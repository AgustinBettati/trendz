import { API_BASE_URL } from '../constants/constants'
import { request } from './APIUtils'
import { User } from '../components/types/types'

export function getAllUsers(): Promise<User[]> {
  //ignore until login is developed
  // if (!localStorage.getItem(ACCESS_TOKEN)) {
  //   return Promise.reject("No access token set.");
  // }

  return request({
    url: API_BASE_URL + "/user",
    method: 'GET'
  });
}

export function addUser(name : String, surname : String, email : String) : Promise<User> {
  return request({
    url : API_BASE_URL + "/user",
    method : 'POST',
    body : JSON.stringify({'email' : email, 'name' : name, 'surname' : surname})
  })
}

