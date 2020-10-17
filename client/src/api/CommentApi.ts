import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'

export function createComment(content: string, postId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/post/"+postId+"/comment",
        method: 'POST',
        body: JSON.stringify({'content': content}),
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token'),'Content-Type': 'application/json'}
    });
}

export function deleteComment(commentId: number) {
    return request({
        url: API_BASE_URL + "/comment/" + commentId,
        method: 'DELETE',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    })
}

export function editComment(content: string, commentId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/comment/" + commentId,
        method: 'PUT',
        body: JSON.stringify({'content': content}),
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token'),'Content-Type': 'application/json'}
    });
}
