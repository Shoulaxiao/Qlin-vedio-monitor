import axios from 'axios'

// 创建 axios 实例
const service = axios.create({
    baseURL: process.env.NODE_ENV === 'production' ? `/videoMonitor/api` : '/api',
    timeout: 30000 // 请求超时时间
})

const err = (error) => {
    if (error.response) {
        if (error.response.status === 403) {
            uni.showToast({
                title:'Forbidden',
                icon: 'error',
                duration:2000
            })
        }
        if (error.response.status === 500) {
            uni.showToast({
                title:'Internal Server Error',
                icon: 'error',
                duration:2000
            })
        }
    }
    return Promise.reject(error)
}

// request interceptor
service.interceptors.request.use(config => {
    // const token = Vue.ls.get(ACCESS_TOKEN)
    // if (token) {
    //     config.headers['Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
    // }
    return config
}, err)

// response interceptor
service.interceptors.response.use((response) => {
    if (response.config.responseType == 'arraybuffer') {
        return response.data
    }  else {
        return response.data
    }
}, err)


const installer = {
    vm: {},
    install(Vue, router = {}) {
        Vue.use(VueAxios, router, service)
    }
}
export {
    installer as VueAxios,
    service as axios
}