import React, {Component} from 'react'
import "./Login.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {NavLink, withRouter} from "react-router-dom";
import {loginUser} from "../../api/UserApi";
import {RouteComponentProps} from 'react-router-dom';

export type Props = RouteComponentProps<any> & {}

export type State = {
    errorMessage: string,
    successMessage: string,
    emailTouched: boolean,
    passwordTouched: boolean,
}

const loginSchema = yup.object({
    email: yup.string().required('Email cannot be empty').email('Invalid email'),
    password: yup.string().required('Password cannot be empty')
})

class Login extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            errorMessage: '',
            successMessage: '',
            emailTouched: false,
            passwordTouched: false,
        }
    }

    handleLogin = (email: string, password: string) => {
        loginUser(email, password, 'user')
            .then((res) => {
                this.setState({errorMessage: '', successMessage: 'User successfully logged in'});
                localStorage.setItem('token', res.token);
                this.props.history.push('/main/home');
            })
            .catch((err) => {
                 if (err.status === 401|| err.status===404)
                    this.setState({successMessage: '', errorMessage: 'Invalid Credentials'});
                else this.setState({successMessage: '', errorMessage: 'An error occurred logging into Trendz!'});
            })
    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: '', successMessage: ''})
        switch (prop){
            case 'email':
                this.setState({emailTouched: true});
                break;
            case 'password':
                this.setState({passwordTouched: true});
                break;
        }
    }

    handleOnBlur = (prop: string) => {
        switch (prop){
            case 'email':
                this.setState({emailTouched: false});
                break;
            case 'password':
                this.setState({passwordTouched: false});
                break;
        }
    }

    render() {
        return (
            <div className={"main-container"}>
                <div className={'login-card'}>
                    <div className={'login-header'}>
                        <img className={'trendz-logo'} src={logo} alt={''}/>
                        <div className={'divisor'}/>
                        <div className={'login-title'}>Login</div>
                    </div>
                    <Formik
                        initialValues={{email: '', password: ''}}
                        onSubmit={values => this.handleLogin(values.email, values.password)}
                        validationSchema={loginSchema}
                    >
                        {(props) => (
                            <div className={'form-container'} onKeyDown={(e) => {
                                if(e.key === 'Enter') {
                                    props.values.email === ''  && props.values.password === '' ?
                                        this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()
                                }
                            }}>
                                <div className={'login-body'}>
                                    <div className={'login-field'}>
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
                                    <div className={'login-field'}>
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
                                </div>
                                <div className={'login-footer'}>
                                    <TrendzButton
                                        title={'Submit'}
                                        onClick={() => props.values.email === ''  && props.values.password === '' ?
                                            this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()}
                                        disabled={!!(props.errors.email  || props.errors.password )}
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
                    <div style={{fontFamily: 'Bitter, sans-serif'}}>
                        Don't have an account? Create one
                        <NavLink to="/register" className="register-link">here</NavLink>
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(Login)