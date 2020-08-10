import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {User} from '../models/User';
import {HttpClient} from '@angular/common/http';
import {AppSettings } from '../settings';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(AppSettings.SERVER_URL + 'user');
  }

}
