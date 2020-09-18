import {request} from "./APIUtils";
import {API_BASE_URL} from "../constants/constants";

export function createTopic(title: string, description: string): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic",
        method: 'POST',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'},
        body: JSON.stringify({'title' : title, 'description': description, 'date': new Date()})
    });
}

export function deleteTopic(id: number): Promise<any> {
    return request({
        url: API_BASE_URL + "/topic/" + id,
        method: 'DEL',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}