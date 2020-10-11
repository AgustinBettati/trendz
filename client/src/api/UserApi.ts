import {API_BASE_URL} from '../constants/constants'
import {request} from './APIUtils'
import {UserType} from '../components/types/types'

export function getAllUsers(): Promise<UserType[]> {
    return request({
        url: API_BASE_URL + "/user",
        method: 'GET'
    });
}

export function registerUser(email: string, username: string, password: string, role: string): Promise<UserType[]> {
    return request({
        url: API_BASE_URL + "/user",
        method: 'POST',
        body: JSON.stringify({'email': email, 'username': username, 'password': password, 'role': role})
    });
}

export function loginUser(email: string, password: string, role: string ): Promise<any> {
    return request({
        url: API_BASE_URL + "/login",
        method: 'POST',
        body: JSON.stringify({'email': email, 'password': password,'role': role })
    });
}

export function deleteUser(): Promise<any> {
    return request({
        url: API_BASE_URL + "/user",
        method: 'DELETE',
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token')}
    });
}

export function getUserData(id: string): Promise<any> {
    return request({
        url: API_BASE_URL + "/user/" + id,
        method: 'GET',
        headers:{'Authorization': 'Bearer '+ localStorage.getItem('token')}
    });
}

export function editUserData(username: string | null, oldPassword: string | null, newPassword: null | string) : Promise<any> {
    return request({
        url: API_BASE_URL + "/user",
        method: 'PUT',
        headers: {'Authorization': 'Bearer ' + localStorage.getItem('token'), 'Content-Type': 'application/json'},
        body: JSON.stringify({'username': username, 'oldPassword': oldPassword,'newPassword': newPassword  })
    })
}
