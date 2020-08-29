import React, { Component } from 'react'
import { User } from '../types/types'
import { TrendzButton} from "../common/TrendzButton/TrendzButton";
import {TrendzCard} from "../common/TrendzCard/TrendzCard";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import { Redirect } from 'react-router-dom';



export type Props = {
    users?: User[],

}

type State = {



}



class ProfileEditor extends Component<Props, State> {

    constructor(props: Props) {
        super(props);



    };














    render() { return (

        <TrendzCard height={200}title={'Profile Editor'}>
       <div>
            <h2>Profile Editor</h2>
            <h3> TO DO</h3>
            </div>
        </TrendzCard>
    );
    }


}

export default ProfileEditor
