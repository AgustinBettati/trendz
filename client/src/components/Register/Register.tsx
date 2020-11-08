import React, {Component} from 'react'
import "./Register.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {registerUser} from "../../api/UserApi";
import {NavLink,RouteComponentProps} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";

export type Props =  RouteComponentProps<any>

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
                toast('Account successfully created!', {onClose: () => this.props.history.push('/')})
            })
            .catch((err) => {
                if (err.status === 409) toast.error(err.message)
                else if (err.status === 401) toast.error('Invalid Credentials')
                else toast.error('An error occurred creating your account!')
            })
    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: '', successMessage: ''})
        switch (prop){
            case 'email':
                this.setState({emailTouched: true});
                break;
            case 'username':
                this.setState({usernameTouched: true});
                break;
            case 'password':
                this.setState({passwordTouched: true});
                break;
            case 'confirmPassword':
                this.setState({confirmPasswordTouched: true});
                break;
        }
    }

    handleOnBlur = (prop: string) => {
        switch (prop){
            case 'email':
                this.setState({emailTouched: false});
                break;
            case 'username':
                this.setState({usernameTouched: false});
                break;
            case 'password':
                this.setState({passwordTouched: false});
                break;
            case 'confirmPassword':
                this.setState({confirmPasswordTouched: false});
                break;
        }
    }

    render() {
        return (
            <div className={"main-container"}>
                <ToastContainer position={"top-right"} autoClose={2500}/>
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
                                            onFocus={() => this.handleOnFocus('email')}
                                            onBlur={() => !props.errors.email && this.handleOnBlur('email')}
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
                                            onFocus={() => this.handleOnFocus('username')}
                                            onBlur={() => !props.errors.username && this.handleOnBlur('username')}
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
                                            onFocus={() => this.handleOnFocus('password')}
                                            onBlur={() => !props.errors.password && this.handleOnBlur('password')}
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
                                            onFocus={() => this.handleOnFocus('confirmPassword')}
                                            onBlur={() => !props.errors.confirmPassword && this.handleOnBlur('confirmPassword')}
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
                                </div>
                            </div>
                        )}
                    </Formik>
                    <div style={{fontFamily: 'Bitter, sans-serif', marginTop: 10}}>
                        Already registered?
                        <NavLink to="/" className="register-link">Login</NavLink>
                    </div>
                </div>
            </div>
        )
    }
}

export default Register
