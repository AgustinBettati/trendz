import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'

export function upvotePost(postId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/post/"+postId+"/upvote",
        method: 'POST',
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token'),'Content-Type': 'application/json'}
    });
}
export function downvotePost(postId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/post/" + postId + "/downvote",
        method: 'POST',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}



