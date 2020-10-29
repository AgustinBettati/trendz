import {request} from "./APIUtils";
import {API_BASE_URL} from "../constants/constants";

export function search(toSearch: string): Promise<any> {
    return request({
        url: API_BASE_URL + "/search?title=" + toSearch,
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'}
    });
}