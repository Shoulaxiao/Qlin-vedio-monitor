const path = require("path");
const {set} = require("vue");
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV);

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    chainWebpack: config => {
        config.resolve.alias
            .set("@", resolve("src"))
            .set("svg", resolve("src/static/svg"));
    },
    css: {
        extract: IS_PROD,
        requireModuleExtension: false,// 去掉文件名中的 .module
        loaderOptions: {
            // 给 less-loader 传递 Less.js 相关选项
            less: {
                // `globalVars` 定义全局对象，可加入全局变量
                globalVars: {
                    primary: '#333'
                }
            }
        }
    },
    transpileDependencies: ['@dcloudio/uni-ui'],

    devServer: {
        hot: true,
        port: 8002,
        proxy: {
            '/api': {
                target: "http://localhost:8899/videoMonitor",
                changeOrigin: true,
                pathRewrite: {
                    "^/api": "/api"
                }
            },
            '/wsserver': {
                target: 'ws://localhost:8899/rms',
                ws: true,
                changeOrigin: true,
            },
        }
    },
}