import React, {Component} from 'react'
import {User} from '../types/types'
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {Link, Redirect} from 'react-router-dom';
import './Profile.css';


export type Props = {
    users?: User[],

}

class Profile extends Component<Props> {

    constructor(props: Props) {
        super(props);
    };


    render() {
        return (

            <div className={'container'}>
                <div className={'profile-card'}>
                    <div className={'title'}>
                        Profile
                    </div>

                    <TrendzInput value={"Sofia"} disabled={true} label={"Username"}/>

                    <TrendzInput value={"sofiasdz@ing.austral.edu.ar"} disabled={true} label={"Email"}/>
                    <div className={'button-c'}>
                        <a href="http://localhost:3000/editProfile" className="button">Edit</a>
                    </div>

                </div>
            </div>

        );
    }


}

export default Profile
