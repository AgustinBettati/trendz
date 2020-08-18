import React, { Component } from 'react'
import { User } from '../types/types'
import "./UserList.css"
import {addUser, getAllUsers} from "../../api/UserApi";


export type Props = {
    user: User,

}

type State = {
    EditOn: boolean
    NewName: string
    NewEmail: string

}



class Profile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            EditOn: false,
            NewName:'',
            NewEmail:''
        }
        this.handleChangeUserName = this.handleChangeUserName.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    };

    handleChangeUserName(event: { target: { value: any; }; }) {
        this.setState({NewName: event.target.value});
    }



    handleSubmit(event: { preventDefault: () => void; }) {
        alert('A change was made ')
        event.preventDefault();
    }







render() { return (
            <form onSubmit={this.handleSubmit}>
                <h2>
                    Profile:
                </h2>
                <label>
                    UserName:
                    <input type="text" value={this.state.NewName} onChange={this.handleChangeUserName} />
                </label>
                <label>
                    Email:
                    <input type="text" value={this.state.NewEmail } />
                </label>
                <input type="submit" value="Submit" />
                <button onClick= {()=>this.setState( {EditOn: !this.state.EditOn})}>
                    Edit
                </button>
            </form>
        );
    }


}

export default Profile
