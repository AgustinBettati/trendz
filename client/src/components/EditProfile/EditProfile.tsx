import React, {Component} from 'react'
import "./EditProfile.css";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {Formik} from 'formik';
import * as yup from 'yup';
import {editUserData, getUserData} from "../../api/UserApi";
import {parseJwt} from "../Routing/utils";
import {NavLink, RouteComponentProps} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";

export type Props = RouteComponentProps<any>

export type State = {
    username: string,
    email: string,
    errorMessage: string,
    successMessage: string,
    usernameTouched: boolean,
    oldPasswordTouched: boolean,
    newPasswordTouched: boolean,
    confirmNewPasswordTouched: boolean,
}

const registerSchema = yup.object().shape({
    username: yup.string().required('Username cannot be empty').max(30, 'Password must have at most 30 characters'),
    oldPassword: yup.string()
        .when('newPassword',
            {
                is: (newPassword) => newPassword,
                then: yup.string().required('Old password cannot be empty'),
                otherwise: yup.string()
            }),
    newPassword: yup.string()
        .when('oldPassword',
            {
                is: (oldPassword) => oldPassword,
                then: yup.string().required('New password cannot be empty').min(8, 'Password must have at least 8 characters'),
                otherwise: yup.string()
            }),
    confirmNewPassword: yup.string()
        .when(['oldPassword', 'newPassword'],
            {
                is: (oldPassword, newPassword) => oldPassword || newPassword,
                then: yup.string().required('Password confirmation is required').oneOf([yup.ref('newPassword')], 'Passwords must match'),
                otherwise: yup.string()
            }
        )
}, [['oldPassword', 'newPassword']])

export class EditProfile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            username: '',
            email: '',
            errorMessage: '',
            successMessage: '',
            usernameTouched: false,
            oldPasswordTouched: false,
            newPasswordTouched: false,
            confirmNewPasswordTouched: false,
        }
    }

    componentDidMount() {
        getUserData(parseJwt(localStorage.getItem('token')).userId)
            .then((res) => {
                this.setState({username: res.userInfo.username, email: res.userInfo.email})
            })
            .catch(() => this.setState({username: 'Unknown', email: 'Unknown'}))
    }

    handleEdit = (username: string, oldPassword: string, newPassword: string) => {

        editUserData(username === this.state.username ? null : username,
            oldPassword === '' ? null : oldPassword,
            newPassword === '' ? null : newPassword)
            .then(() => {
                this.setState({errorMessage: '', successMessage: 'User edited successfully'});
                toast('Profile successfully edited!', {onClose: () => this.props.history.push('/main/profile')})
            })
            .catch((err) => {
                if (err.status === 409) toast.error('Username already in use')
                else if (err.status === 401) toast.error('Incorrect password')
                else toast.error('An error occurred editing your profile!')
            })
    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: '', successMessage: ''})
        switch (prop){
            case 'username':
                this.setState({usernameTouched: true});
                break;
            case 'oldPassword':
                this.setState({oldPasswordTouched: true});
                break;
            case 'newPassword':
                this.setState({newPasswordTouched: true});
                break;
            case 'confirmNewPassword':
                this.setState({confirmNewPasswordTouched: true});
                break;
        }
    }

    handleOnBlur = (prop: string) => {
        switch (prop){
            case 'username':
                this.setState({usernameTouched: false});
                break;
            case 'oldPassword':
                this.setState({oldPasswordTouched: false});
                break;
            case 'newPassword':
                this.setState({newPasswordTouched: false});
                break;
            case 'confirmNewPassword':
                this.setState({confirmNewPasswordTouched: false});
                break;
        }
    }

    render() {
        return (
            <div className={"main-container"}>
                <ToastContainer position={"top-center"} autoClose={2500}/>
                <div className={'edit-card'}>
                    {
                        this.state.username === '' ?
                            <div>Loading...</div> :
                            <div style={{display: 'flex', flexDirection: 'column'}}>
                                <div className={'edit-title'}>Edit Profile</div>
                                <Formik
                                    initialValues={{username: this.state.username, oldPassword: '', newPassword: '', confirmNewPassword: ''}}
                                    onSubmit={values => this.handleEdit(values.username,values.oldPassword,values.newPassword)}
                                    validationSchema={registerSchema}
                                >
                                    {(props) => (
                                        <div className={'form-container'}>
                                            <div className={'edit-body'}>
                                                <div className={'register-field'} style={{marginBottom: 20}}>
                                                    <TrendzInput
                                                        label={'Email'}
                                                        value={this.state.email}
                                                        disabled={true}
                                                    />
                                                </div>
                                                <div className={'register-field'}>
                                                    <TrendzInput
                                                        label={'Username'}
                                                        onChange={props.handleChange('username')}
                                                        value={props.values.username}
                                                        onFocus={() => this.handleOnFocus('username')}
                                                        onBlur={() => !props.errors.username && this.handleOnBlur('username')}
                                                    />
                                                    <div className={'error-message'}>{this.state.usernameTouched && props.errors.username}</div>
                                                </div>
                                                <div className={'register-field'}>
                                                    <TrendzInput
                                                        placeholder={'Old password'}
                                                        label={'Old password'}
                                                        password={true}
                                                        onChange={props.handleChange('oldPassword')}
                                                        value={props.values.oldPassword}
                                                        onFocus={() => this.handleOnFocus('oldPassword')}
                                                        onBlur={() => !props.errors.oldPassword && this.handleOnBlur('oldPassword')}
                                                    />
                                                    <div
                                                        className={'error-message'}>{this.state.oldPasswordTouched && props.errors.oldPassword}</div>
                                                </div>
                                                <div className={'register-field'}>
                                                    <TrendzInput
                                                        placeholder={'New password'}
                                                        label={'New password'}
                                                        password={true}
                                                        onChange={props.handleChange('newPassword')}
                                                        value={props.values.newPassword}
                                                        onFocus={() => this.handleOnFocus('newPassword')}
                                                        onBlur={() => !props.errors.newPassword && this.handleOnBlur('newPassword')}
                                                    />
                                                    <div
                                                        className={'error-message'}>{this.state.newPasswordTouched && props.errors.newPassword}</div>
                                                </div>
                                                <div className={'register-field'}>
                                                    <TrendzInput
                                                        placeholder={'Confirm new password'}
                                                        label={'Confirm new password'}
                                                        password={true}
                                                        onChange={props.handleChange('confirmNewPassword')}
                                                        value={props.values.confirmNewPassword}
                                                        onFocus={() => this.handleOnFocus('confirmNewPassword')}
                                                        onBlur={() => !props.errors.confirmNewPassword && this.handleOnBlur('confirmNewPassword')}
                                                    />
                                                    <div
                                                        className={'error-message'}>{this.state.confirmNewPasswordTouched && props.errors.confirmNewPassword}</div>
                                                </div>
                                            </div>
                                            <div className={'edit-footer'}>
                                                <TrendzButton
                                                    title={'Save changes'}
                                                    onClick={() => props.values.username === '' || (props.values.username === this.state.username && props.values.newPassword === '' && props.values.oldPassword === '' && props.values.confirmNewPassword === '') ?
                                                        this.setState({errorMessage: 'Please, complete or change fields before submitting'}) : props.handleSubmit()}
                                                    disabled={!!(props.errors.username || props.errors.oldPassword || props.errors.newPassword || props.errors.confirmNewPassword)}
                                                />
                                                <div style={{
                                                    height: 20,
                                                    display: 'flex',
                                                    flexDirection: 'column',
                                                    alignItems: 'center',
                                                    marginTop: 10
                                                }}>
                                                    {
                                                        this.state.errorMessage !== '' &&
                                                        <div className={'error-message'}>{this.state.errorMessage}</div>
                                                    }
                                                    {
                                                        this.state.successMessage !== '' &&
                                                        <div className={'success-message'}>{this.state.successMessage}</div>
                                                    }
                                                </div>
                                            </div>
                                            <div style={{fontFamily: 'Bitter, sans-serif'}}>
                                                Go to
                                                <NavLink to="/main/profile" className="register-link">Profile</NavLink>
                                            </div>
                                        </div>
                                    )}
                                </Formik>
                            </div>
                    }
                </div>
            </div>
        )
    }
}

export default EditProfile;
