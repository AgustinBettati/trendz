import React, {Component} from 'react'
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import './Profile.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {getUserData} from "../../api/UserApi";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {parseJwt} from "../Routing/utils";

export type Props = RouteComponentProps<any> & {}

export type State = {
    username: string,
    email: string
}

class Profile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            username: '',
            email: ''
        }
    };

    componentDidMount() {
        getUserData(parseJwt(localStorage.getItem('token')).userId)
            .then((res) => {
                this.setState({username: res.username, email: res.email})
            })
            .catch(() => this.setState({username: 'Unknown', email: 'Unknown'}))
    }

    handleDelete() {

    }

    render() {
        return (
            <div className={'container'}>
                <div className={'profile-card'}>
                    <div className={'title'}>
                        Profile
                    </div>
                    <div style={{marginBottom: 10}}>
                        <TrendzInput value={this.state.username} disabled={true} label={"Username"}/>
                    </div>
                    <TrendzInput value={this.state.email} disabled={true} label={"Email"}/>
                    <div style={{display: 'flex', flexDirection: 'row', marginTop: 20, width: '70%', justifyContent: 'space-around'}}>
                        <TrendzButton title={'Edit'} onClick={() => this.props.history.push('/main/editProfile')}/>
                        <TrendzButton title={'Delete'} onClick={() => this.handleDelete()}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Profile)
