import {axios} from '@/utils/request'

export default {
    async queryVideosListPage(params) {
        return await axios({
            url: '/video/queryVideosListPage',
            method: 'post',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
            data: params
        })
    },

    async queryDetailById(id) {
        return await axios({
            url: '/video/queryDetailById?id='+id,
            method: 'get'
        })
    },

    async getLiveUrl() {
        return await axios({
            url: '/video/getLiveUrl',
            method: 'get'
        })
    }
}