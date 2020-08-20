import React, { Component } from 'react'
import { User } from '../types/types'
import { TrendzButton} from "../common/TrendzButton/TrendzButton";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import { Redirect } from 'react-router-dom';


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

    <div>
        {this.state.EditOn ?
            <Redirect to="/edit" push/> :
            <article >
            <form onSubmit={this.handleSubmit}>
                <h2>
                    Profile:
                </h2>

                <TrendzInput value={"Sofia"} disabled={true} label={"Username"}/>

                <TrendzInput value={"sofiasdz@ing.austral.edu.ar"} disabled={true} label={"Email"}/>


                <TrendzButton title={'Edit'} onClick={() => this.setState({EditOn: !this.state.EditOn})}/>

            </form>
                </article>
        }</div>

        );
    }


}

export default Profile
