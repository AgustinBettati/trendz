import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'
import {Post} from '../components/types/post'


export function createPost(title: string, description: string, link: string, topicId: number): Promise<Post[]> {
    return request({
        url: API_BASE_URL + "/post",
        method: 'POST',
        body: JSON.stringify({'title': title, 'description': description, 'link': link, 'topicId': topicId}),
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token')}
    });
}