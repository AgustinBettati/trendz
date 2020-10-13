import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'
import {Comment} from '../components/types/comment'


export function createComment(content: string,postId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/post/"+postId+"/comment",
        method: 'POST',
        body: JSON.stringify({'content': content}),
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token'),'Content-Type': 'application/json'}
    });
}


