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