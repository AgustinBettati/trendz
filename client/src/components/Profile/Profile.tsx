import React, {Component} from 'react'
import {User} from '../types/types'
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import './Profile.css';
import {NavLink} from "react-router-dom";


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
                        <NavLink to="/editProfile" className="button">Edit</NavLink>
                    </div>

                </div>
            </div>

        );
    }


}

export default Profile
