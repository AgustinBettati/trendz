import React, { Component } from 'react'
import { User } from '../types/types'
import "./UserList.css"

export type Props = {
  users?: User[],
}

export class UserList extends Component<Props> {
  render() {
    const { users } = this.props
    const usersItems = users && users.map(user => {
      return (
        <li className="list-group-item">
          <span>{user.name}</span>
        </li>
      )
    })
    return (
      <div className="container">
        <h2>Lista de usuarios</h2>
        <ul className="list-group user-list">
          {usersItems}
        </ul>
      </div>
    )
  }
}

export default UserList
