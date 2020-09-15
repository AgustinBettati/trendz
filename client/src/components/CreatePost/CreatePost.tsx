import React, {Component} from 'react'
import "./CreatePost.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzMultilineInput} from "../common/TrendzMultilineInput/TrendzMultilineInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {withRouter} from "react-router-dom";
import {RouteComponentProps} from 'react-router-dom';

export type Props = RouteComponentProps<any> & {}

export type State = {
    errorMessage: string,
    successMessage: string,
    titleTouched: boolean,
    descriptionTouched: boolean,
    linkTouched: boolean,
}

const createPostSchema = yup.object({
    title: yup.string().required('Title cannot be empty').max(60,"Title can be up to 60 characters long").min(2,"Title must be at least 2 characters long"),
    description: yup.string(),
})

class CreatePost extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            errorMessage: '',
            successMessage: '',
            titleTouched: false,
            descriptionTouched: false,
            linkTouched: false,
        }
    }

    handleSubmitPost = (title: string, description: string, link: string) => {
        /*     loginUser(title, description, 'user')
                 .then((res) => {
                     this.setState({errorMessage: '', successMessage: 'User successfully logged in'});
                     localStorage.setItem('token', res.token);
                     this.props.history.push('/main/home');
                 })
                 .catch(() => {
                     this.setState({successMessage: '', errorMessage:'Invalid Credentials'});
                 })*/
    }

    private handleCancel() {
        this.props.history.push('/main/home');
    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: '', successMessage: ''})
        switch (prop){
            case 'title':
                this.setState({titleTouched: true});
                break;
            case 'description':
                this.setState({descriptionTouched: true});
                break;
            case 'link':
                this.setState({linkTouched: true});
                break;
        }
    }

    handleOnBlur = (prop: string) => {
        switch (prop){
            case 'title':
                this.setState({titleTouched: false});
                break;
            case 'description':
                this.setState({descriptionTouched: false});
                break;
            case 'link':
                this.setState({linkTouched: false});
                break;
        }
    }

    render() {
        return (
            <div className={"main-container"}>
                <div className={'createpost-card'}>
                    <div className={'createpost-header'}>
                        <img className={'trendz-logo'} src={logo} alt={''}/>
                        <div className={'divisor'}/>
                        <div className={'createpost-title'}>Create Post</div>
                    </div>
                    <Formik
                        initialValues={{title: '', description: '',link: ''}}
                        onSubmit={values => this.handleSubmitPost(values.title, values.description,values.link)}
                        validationSchema={createPostSchema}
                    >
                        {(props) => (
                            <div className={'form-container'}>
                                <div className={'createpost-body'}>
                                    <div className={'createpost-field'}>
                                        <TrendzInput
                                            width='775px'
                                            placeholder={'Title'}
                                            label={'Title'}
                                            onChange={props.handleChange('title')}
                                            value={props.values.title}
                                            onFocus={() => this.handleOnFocus('title')}
                                            onBlur={() => !props.errors.title && this.handleOnBlur('title')}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.titleTouched && props.errors.title}</div>
                                    </div>
                                    <div className={'createpost-field'}>
                                        <TrendzMultilineInput
                                            placeholder={'Description'}
                                            label={'Description'}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.descriptionTouched && props.errors.description}</div>
                                    </div>
                                    <div className={'createpost-field'}>
                                        <TrendzInput
                                            width='775px'
                                            placeholder={'Link'}
                                            label={'Link'}
                                            onChange={props.handleChange('link')}
                                            value={props.values.link}
                                            onFocus={() => this.handleOnFocus('link')}
                                            onBlur={() => !props.errors.link && this.handleOnBlur('link')}
                                        />
                                        <div
                                            className={'error-message'}>{this.state.linkTouched && props.errors.link}</div>
                                    </div>
                                </div>
                                <div className={'createpost-footer'}>
                                    <div>

                                        {
                                            this.state.errorMessage !== '' &&
                                            <div className={'error-message'}>{this.state.errorMessage}</div>
                                        }
                                        {
                                            this.state.successMessage !== '' &&
                                            <div className={'success-message'}>{this.state.successMessage}</div>
                                        }

                                    </div>
                                    <TrendzButton
                                        title={'Submit'}
                                        onClick={() => props.values.title === ''  && props.values.description === '' ?
                                            this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()}
                                        disabled={!!(props.errors.title  || props.errors.description )}
                                    />
                                    <div style={{
                                        height: 20,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        alignItems: 'center',
                                        marginTop: 10
                                    }}>

                                    </div>
                                    <TrendzButton
                                        title={'Cancel'}
                                        onClick={()=>this.handleCancel() }
                                    />
                                </div>
                            </div>
                        )}
                    </Formik>
                </div>
            </div>
        )
    }


}

export default withRouter(CreatePost)