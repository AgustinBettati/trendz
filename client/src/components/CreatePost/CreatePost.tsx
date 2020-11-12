import React, {Component} from 'react'
import "./CreatePost.css";
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzMultilineInput} from "../common/TrendzMultilineInput/TrendzMultilineInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {withRouter} from "react-router-dom";
import {RouteComponentProps} from 'react-router-dom';
import {createPost} from "../../api/PostApi";
import {toast} from "react-toastify";

export type Props = RouteComponentProps<any> & {}

export type State = {
    errorMessage: string,
    titleTouched: boolean,
    descriptionTouched: boolean,
    linkTouched: boolean,
}

const createPostSchema = yup.object({
    title: yup.string().required('Title cannot be empty').max(60,"Title can be up to 60 characters long").min(2,"Title must be at least 2 characters long"),
    description: yup.string().max(40000, "Description can be up to 40000 characters long"),
})

class CreatePost extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            errorMessage: '',
            titleTouched: false,
            descriptionTouched: false,
            linkTouched: false,
        }
    }

    handleSubmitPost = (title: string, description: string, link: string) => {
        let topic = this.props.location.state.topic;
         createPost(title, description, link, topic.id,"post")
             .then(() => {
                 this.setState({errorMessage: ''});
                 this.props.history.push('/main/topic/' + topic.id, {topic: topic})
                 toast('Post successfully created!')
             })
             .catch((err) => {
                 if (err.status === 409) this.setState({errorMessage: 'Title already in use'});
                 else this.setState({errorMessage: 'An error occurred creating the post!'});
             })
    }

    private handleCancel() {
        let topic = this.props.location.state.topic;
        this.props.history.push('/main/topic/' + topic.id, {topic: topic});
    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: ''})
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
                                            onChange={props.handleChange('description')}
                                            value={props.values.description}
                                            onFocus={() => this.handleOnFocus('description')}
                                            onBlur={() => !props.errors.description && this.handleOnBlur('description')}
                                            rows={5}
                                            cols={77}
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
                                    <div className={'error-message'}>{this.state.errorMessage}</div>
                                    <div className={'footer-buttons'}>
                                        <TrendzButton
                                            title={'Submit'}
                                            onClick={() => props.values.title === ''  && props.values.description === '' ?
                                                this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()}
                                            disabled={!!(props.errors.title  || props.errors.description )}
                                        />
                                        <TrendzButton
                                            title={'Cancel'}
                                            onClick={()=>this.handleCancel() }
                                        />
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

export default withRouter(CreatePost)