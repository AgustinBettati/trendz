import React, {Component} from 'react'
import {User} from '../types/types'
import "./UserList.css"
import {TrendzButton} from "../common/TrendzButton/TrendzButton";

export type Props = {
    users?: User[],
}

type State = {
    showUserList: boolean
}

export class UserList extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            showUserList: false
        }
    }

    render() {
        const {users} = this.props
        const usersItems = users && users.map(user => {
            return (
                <li className="list-group-item">
                    <span>{user.name}</span>
                    <span>{' ' + user.surname}</span>
                </li>
            )
        })
        return (
            <div className="container">
                <div className={'header-container'}>
                    <h2>Lista de usuarios</h2>
                    <TrendzButton
                        title={'Toggle list'}
                        onClick={() => this.setState({showUserList: !this.state.showUserList})}
                    />
                </div>
                {
                   this.state.showUserList &&
                   <ul className="list-group user-list">
                       {usersItems}
                   </ul>
                }
            </div>
        )
    }
}

export default UserList
