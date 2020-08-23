import React, {Component} from 'react'
import "./Register.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {registerUser} from "../../api/UserApi";

export type Props = {}

export type State = {
    errorMessage: string,
    successMessage: string,
    emailTouched: boolean,
    usernameTouched: boolean,
    passwordTouched: boolean,
    confirmPasswordTouched: boolean,
}

const registerSchema = yup.object({
    email: yup.string().required('Email is required').email('Invalid email'),
    username: yup.string().required('Username required'),
    password: yup.string().required('Password is required').min(8, 'Password must have at least 8 characters'),
    confirmPassword: yup.string().required('Password confirmation is required').oneOf([yup.ref('password')], 'Passwords must match')
})

export class Register extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            errorMessage: '',
            successMessage: '',
            emailTouched: false,
            usernameTouched: false,
            passwordTouched: false,
            confirmPasswordTouched: false
        }
    }

    handleRegister = (email: string, username: string, password: string) => {
        registerUser(email, username, password, 'user')
            .then(() => {
                this.setState({errorMessage: ''})
                this.setState({successMessage: 'User successfully registered'});
            })
            .catch((err) => {
                this.setState({successMessage: ''})
                this.setState({errorMessage: err.message});
            })
    }

    render() {
        return (
            <div className={"main-container"}>
                <div className={'register-card'}>
                    <div className={'register-header'}>
                        <img className={'trendz-logo'} src={logo} alt={''}/>
                        <div className={'divisor'}/>
                        <div className={'register-title'}>Register</div>
                    </div>
                    <Formik
                        initialValues={{email: '', username: '', password: '', confirmPassword: ''}}
                        onSubmit={values => this.handleRegister(values.email, values.username, values.password)}
                        validationSchema={registerSchema}
                    >
                        {(props) => (
                            <div className={'form-container'}>
                                <div className={'register-body'}>
                                    <div className={'register-field'}>
                                        <TrendzInput
                                            placeholder={'Email'}
                                            label={'Email'}
                                            onChange={props.handleChange('email')}
                                            value={props.values.email}
                                            onFocus={() => {
                                                this.setState({emailTouched: true})
                                                this.setState({errorMessage: ''})
                                                this.setState({successMessage: ''})
                                            }}
                                            onBlur={() => !props.errors.email && this.setState({emailTouched: false})}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.emailTouched && props.errors.email}</div>
                                    </div>
                                    <div className={'register-field'}>
                                        <TrendzInput
                                            placeholder={'Username'}
                                            label={'Username'}
                                            onChange={props.handleChange('username')}
                                            value={props.values.username}
                                            onFocus={() => {
                                                this.setState({usernameTouched: true})
                                                this.setState({errorMessage: ''})
                                                this.setState({successMessage: ''})
                                            }}
                                            onBlur={() => !props.errors.username && this.setState({usernameTouched: false})}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.usernameTouched && props.errors.username}</div>
                                    </div>
                                    <div className={'register-field'}>
                                        <TrendzInput
                                            placeholder={'Password'}
                                            label={'Password'}
                                            password={true}
                                            onChange={props.handleChange('password')}
                                            value={props.values.password}
                                            onFocus={() => {
                                                this.setState({passwordTouched: true})
                                                this.setState({errorMessage: ''})
                                                this.setState({successMessage: ''})
                                            }}
                                            onBlur={() => !props.errors.password && this.setState({passwordTouched: false})}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.passwordTouched && props.errors.password}</div>
                                    </div>
                                    <div className={'register-field'}>
                                        <TrendzInput
                                            placeholder={'Confirm password'}
                                            label={'Confirm password'}
                                            password={true}
                                            onChange={props.handleChange('confirmPassword')}
                                            value={props.values.confirmPassword}
                                            onFocus={() => {
                                                this.setState({confirmPasswordTouched: true})
                                                this.setState({errorMessage: ''})
                                                this.setState({successMessage: ''})
                                            }}
                                            onBlur={() => !props.errors.confirmPassword && this.setState({confirmPasswordTouched: false})}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.confirmPasswordTouched && props.errors.confirmPassword}</div>
                                    </div>
                                </div>
                                <div className={'register-footer'}>
                                    <TrendzButton
                                        title={'Submit'}
                                        onClick={() => props.values.email === '' && props.values.username === '' && props.values.password === '' ?
                                            this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()}
                                        disabled={!!(props.errors.email || props.errors.username || props.errors.password || props.errors.confirmPassword)}
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
                            </div>
                        )}
                    </Formik>
                </div>
            </div>
        )
    }
}

export default Register
