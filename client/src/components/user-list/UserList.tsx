import React, { Component } from 'react'
import { User } from '../types/types'
import "./UserList.css"

export type Props = {
  users?: User[],
}

type State = {
    hidden: boolean
}

export class UserList extends Component<Props,State> {
  constructor(props : Props) {
    super(props);
    this.state = {
      hidden : false
    }
  }

  handleChange() {
    const {hidden} = this.state;
    this.setState({hidden : !hidden})
  }

  render() {
    const { users } = this.props
    const { hidden } = this.state;
    const usersItems = users && users.map(user => {
      return (
        <li className="list-group-item">
          <span>{user.name + " "}</span>
          <span>{user.surname}</span>
        </li>
      )
    })

    const toggle =
    <div>
      <h3>Mostrar lista de usuarios </h3>
      <label className="switch">
        <input type="checkbox" onChange={() => this.handleChange()} checked={!this.state.hidden}/>
        <span className="slider round"/>
      </label>
    </div>;


    if (hidden) {
        return (
            toggle
        )
      }

      return (
      <div>
        {toggle}
        <div className="user_container">
          <h2>Lista de usuarios</h2>
          <ul className="list-group user-list">
            {usersItems}
          </ul>
        </div>
      </div>
    )
  }
}

export default UserList
