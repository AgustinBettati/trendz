import {request} from "./APIUtils";
import {API_BASE_URL} from "../constants/constants";

export function createTopic(title: string, description: string): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic",
        method: 'POST',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'},
        body: JSON.stringify({'title' : title, 'description': description})
    });
}

export function getTopics(page: number, size: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic?page=" + page + '&size=' + size,
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}

export function deleteTopic(id: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic/" + id,
        method: 'DELETE',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}

export function getTopicPosts(id: number, page: number, size: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/topicposts/" + id + '?page=' + page + '&size=' + size,
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}

export function getTopic(id: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic/"+id,
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}
