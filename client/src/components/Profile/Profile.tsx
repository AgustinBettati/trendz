import React, { Component } from 'react'
import { User } from '../types/types'
import { TrendzButton} from "../common/TrendzButton/TrendzButton";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import { Redirect } from 'react-router-dom';
import './Profile.css';


export type Props = {
    users?: User[],

}

type State = {
    EditOn: boolean


}



class Profile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state ={
            EditOn:false

        }


        this.handleSubmit = this.handleSubmit.bind(this);
    };






    handleSubmit(event: { preventDefault: () => void; }) {
        alert('A change was made ')
        event.preventDefault();
    }







render() { return (

<div className={'container'}>
    <div className={'profile-card'}>
        {this.state.EditOn ?
            <Redirect to="/editProfile" push/> :
            <article>
            <form onSubmit={this.handleSubmit}>
                <div className={'title'}>
                    Profile:
                </div>

                <TrendzInput value={"Sofia"} disabled={true} label={"Username"}/>

                <TrendzInput value={"sofiasdz@ing.austral.edu.ar"} disabled={true} label={"Email"}/>


                <TrendzButton title={'Edit'} onClick={() => this.setState({EditOn: !this.state.EditOn})}/>

            </form>
                </article>
        }</div>
</div>

        );
    }


}

export default Profile
