import React, { Component } from 'react'
import { User } from '../types/types'
import "./UserList.css"
import {getAllUsers} from "../../api/UserApi";

export type Props = {
  users?: User[],

}

type State = {
    showList: boolean

}


export class UserList extends Component<Props, State>{
    constructor(props: Props) {
    super(props)
    this.state = {
        showList:true
    }

}




  render() {
      const {showList} = this.state
      const{users}=this.props




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
                  <button onClick= {()=>this.setState( {showList: !this.state.showList})}>
                      showList
                  </button>
                  { showList &&

                      <ul className="list-group user-list">
                          {usersItems}
                      </ul>

                  }

              </div>
          )
      }




}


export default UserList
