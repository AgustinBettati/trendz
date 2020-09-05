import React, {Component} from 'react'
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import './Profile.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {getUserData} from "../../api/UserApi";
import {parseJwt} from "../Routing/utils";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import Modal from 'react-modal';

export type Props = RouteComponentProps<any> & {}

export type State = {
    username: string,
    email: string,
    showModal: boolean
}

class Profile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            username: '',
            email: '',
            showModal: false
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
        this.setState({showModal: true})
    }

    handleConfirm(){

    }

    handleCancel(){
        this.setState({showModal: false})
    }

    render() {
        return (
            <div className={'container'}>
                <Modal
                    isOpen={this.state.showModal}
                    onRequestClose={this.handleCancel.bind(this)}
                    shouldCloseOnOverlayClick={true}
                    className={'modal'}
                    overlayClassName={'overlay'}
                >
                    <div className={'modal-text'}>
                        <span>You are about to delete your profile</span>
                        <span>Do you wish to continue?</span>
                    </div>
                    <div className={'modal-buttons'}>
                        <TrendzButton title={'Confirm'} onClick={this.handleConfirm.bind(this)}/>
                        <TrendzButton title={'Cancel'} color={'#DF6052'} onClick={this.handleCancel.bind(this)}/>
                    </div>
                </Modal>
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
                        <TrendzButton title={'Delete'} color={'#DF6052'} onClick={this.handleDelete.bind(this)}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Profile)
