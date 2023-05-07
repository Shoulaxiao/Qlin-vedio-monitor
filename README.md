# Qlin-vedio-monitor
摄像头监控系统

## 安装STMP服务器
* 拉取nginx-stmp镜像
```docker
docker pull alqutami/rtmp-hls
```
* 运行nginx-stmp容器。$HOME/Documents/nginx/nginx.conf 需要替换为自己电脑上的路径
```docker
docker run -d --name nginx-hls -p 1935:1935 -p 8887:8887 -v $HOME/Documents/nginx/nginx.conf:/etc/nginx/nginx.conf alqutami/rtmp-hls
docker run -d --name nginx-hls -p 1935:1935 -p 8887:8887 -v /d/docker/nginx-rtmp/nginx.conf:/etc/nginx/nginx.conf alqutami/rtmp-hls
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 --name mysql mysql --default-authentication-plugin=mysql_native_password
```
nginx.conf 的配置文件如下：
```
worker_processes  auto;
#error_log  logs/error.log;2023-05-03 13:37:37.349 ERROR [][][][][]execute action error

    
events {
    worker_connections  1024;
}

# RTMP configuration
rtmp {
    server {
		listen 1935; # Listen on standard RTMP port
		chunk_size 4000; 
		# ping 30s;
		# notify_method get;

		# This application is to accept incoming stream
		application live {
			live on; # Allows live input

			# for each received stream, transcode for adaptive streaming
			# This single ffmpeg command takes the input and transforms
			# the source into 4 different streams with different bitrates
			# and qualities. # these settings respect the aspect ratio.
			exec_push  /usr/local/bin/ffmpeg -i rtmp://localhost:1935/$app/$name -async 1 -vsync -1
						-c:v libx264 -c:a aac -b:v 256k  -b:a 64k  -vf "scale=480:trunc(ow/a/2)*2"  -tune zerolatency -preset superfast -crf 23 -f flv rtmp://localhost:1935/show/$name_low
						-c:v libx264 -c:a aac -b:v 768k  -b:a 128k -vf "scale=720:trunc(ow/a/2)*2"  -tune zerolatency -preset superfast -crf 23 -f flv rtmp://localhost:1935/show/$name_mid
						-c:v libx264 -c:a aac -b:v 1024k -b:a 128k -vf "scale=960:trunc(ow/a/2)*2"  -tune zerolatency -preset superfast -crf 23 -f flv rtmp://localhost:1935/show/$name_high
						-c:v libx264 -c:a aac -b:v 1920k -b:a 128k -vf "scale=1280:trunc(ow/a/2)*2" -tune zerolatency -preset superfast -crf 23 -f flv rtmp://localhost:1935/show/$name_hd720
						-c copy -f flv rtmp://localhost:1935/show/$name_src;			
			drop_idle_publisher 10s; 
		}

		# This is the HLS application
		application show {
			live on; # Allows live input from above application
			# deny play all; # disable consuming the stream from nginx as rtmp
			
			hls on; # Enable HTTP Live Streaming
			hls_fragment 3;
			hls_playlist_length 20;
			hls_path /mnt/hls/;  # hls fragments path
			# Instruct clients to adjust resolution according to bandwidth
			hls_variant _src BANDWIDTH=4096000; # Source bitrate, source resolution
			hls_variant _hd720 BANDWIDTH=2048000; # High bitrate, HD 720p resolution
			hls_variant _high BANDWIDTH=1152000; # High bitrate, higher-than-SD resolution
			hls_variant _mid BANDWIDTH=448000; # Medium bitrate, SD resolution
			hls_variant _low BANDWIDTH=288000; # Low bitrate, sub-SD resolution
			
			# MPEG-DASH
            dash on;
            dash_path /mnt/dash/;  # dash fragments path
			dash_fragment 3;
			dash_playlist_length 20;			
		}
	}
}


http {
	sendfile off;
	tcp_nopush on;
	directio 512;
	# aio on;
	
	# HTTP server required to serve the player and HLS fragments
	server {
		listen 8887;
		
		location /images {
		    root /home/images;
		    autoindex_exact_size off; # 显示出文件的确切大小,默认为on开启
            autoindex_localtime on; # 显示的文件时间为文件的服务器时间，off时显示为GMT时间
            charset utf-8,gbk; # 编码设置
		}
		
		# Serve HLS fragments
		location /hls {
			types {
				application/vnd.apple.mpegurl m3u8;
				video/mp2t ts;
			}
			
			root /mnt;

            add_header Cache-Control no-cache; # Disable cache
			
			# CORS setup
			add_header 'Access-Control-Allow-Origin' '*' always;
			add_header 'Access-Control-Expose-Headers' 'Content-Length';
            
			# allow CORS preflight requests
			if ($request_method = 'OPTIONS') {
				add_header 'Access-Control-Allow-Origin' '*';
				add_header 'Access-Control-Max-Age' 1728000;
				add_header 'Content-Type' 'text/plain charset=UTF-8';
				add_header 'Content-Length' 0;
				return 204;
			}
		}
		
        # Serve DASH fragments
        location /dash {
            types {
                application/dash+xml mpd;
                video/mp4 mp4;
            }

			root /mnt;
			
			add_header Cache-Control no-cache; # Disable cache


            # CORS setup
            add_header 'Access-Control-Allow-Origin' '*' always;
            add_header 'Access-Control-Expose-Headers' 'Content-Length';

            # Allow CORS preflight requests
            if ($request_method = 'OPTIONS') {
                add_header 'Access-Control-Allow-Origin' '*';
                add_header 'Access-Control-Max-Age' 1728000;
                add_header 'Content-Type' 'text/plain charset=UTF-8';
                add_header 'Content-Length' 0;
                return 204;
            }
        }		
		
		# This URL provides RTMP statistics in XML
		location /stat {
			rtmp_stat all;
			rtmp_stat_stylesheet stat.xsl; # Use stat.xsl stylesheet 
		}

		location /stat.xsl {
			# XML stylesheet to view RTMP stats.
			root /usr/local/nginx/html;
		}

	}
}

```
## 1.采集摄像头数据命令
进入桌面端docker,运行alqutami/rtmp-hls和mysql实例
## 2.采集摄像头数据命令
-- 安装好ffmpeg工具 --
远程连接树莓派，在终端输入以下命令，注意： rtmp://192.168.0.108:1935/show/camera 的地址响应的IP需要改为nginx-hls流媒体服务器所在机器的IP
```shell
ffmpeg -i /dev/video0 -vcodec libx264 -max_delay 100 -s 640x480 -f flv -an  -g 5 -b 700000 rtmp://192.168.0.108:1935/show/camera
```
看是否推成功，看docker中，nginx-hls的file下面的/mnt
## 3.运行后端程序
 定义IP=192.168.0.108:8899
## 4.运行前端程序
192.168.0.108:8002
```shell
cd front-client
npm run serve
```
## 5.运行温湿度传感器脚本
进入树莓派
发请求的URL对应的IP要等于IP=192.168.0.108:8899