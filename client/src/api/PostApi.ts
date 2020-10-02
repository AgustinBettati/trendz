import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'
import {Post} from '../components/types/post'


export function createPost(title: string, description: string, link: string, topicId: number, role:string): Promise<Post[]> {
    return request({
        url: API_BASE_URL + "/post",
        method: 'POST',
        body: JSON.stringify({'title': title, 'description': description, 'link': link, 'topicId': topicId, 'role': role}),
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token'),'Content-Type': 'application/json'}
    });
}

export function editPost(title: string, description: string, link: string, postId: number, role:string): Promise<Post[]> {
    return request({
        url: API_BASE_URL + "/post/" + postId,
        method: 'PUT',
        body: JSON.stringify({'title': title, 'description': description, 'link': link, 'role': role}),
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}

export function getPostData(postId: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/post/" + postId,
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}

export function deletePost(postId: number) {
    return request({
        url: API_BASE_URL + "/post/" + postId,
        method: 'DELETE',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    })
}


