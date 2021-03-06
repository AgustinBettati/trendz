import React, {Component} from 'react'
import "./EditPost.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzMultilineInput} from "../common/TrendzMultilineInput/TrendzMultilineInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {withRouter} from "react-router-dom";
import {RouteComponentProps} from 'react-router-dom';
import {editPost} from "../../api/PostApi";
import {getPostData} from "../../api/PostApi";
import {toast} from "react-toastify";

export type Props = RouteComponentProps<any> & {}

export type State = {
    title: string,
    description: string,
    link: string,
    postDataLoaded: boolean,
    errorMessage: string,
    titleTouched: boolean,
    descriptionTouched: boolean,
    linkTouched: boolean,
}

const editPostSchema = yup.object({
    title: yup.string().required('Title cannot be empty').max(60,"Title can be up to 60 characters long").min(2,"Title must be at least 2 characters long"),
    description: yup.string(),
})

class EditPost extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            title:'',
            description:'',
            link:'',
            postDataLoaded:false,
            errorMessage: '',
            titleTouched: false,
            descriptionTouched: false,
            linkTouched: false,
        }
    }



    componentDidMount() {
        getPostData(this.props.location.state ? this.props.location.state.post.id : null)
            .then((res:any) => {
                this.setState({title: res.title, description: res.description, link:res.link})
                this.setState({
                    postDataLoaded: true
                });
            })
            .catch(() => {this.setState
                ({title:'Unknown', description:'Unknown', link:'Unknown'})
            this.setState({
                postDataLoaded: true
            });
        })

    }


    handleEditPost = (title: string, description: string, link: string) => {
        let postId = this.props.location.state.post.id;
        editPost(title, description, link, postId,"post")
            .then(() => {
                this.setState({errorMessage: ''});
                this.props.history.push('/main/post/' + postId)
                toast('Post successfully edited!')
            })
            .catch((err) => {
                if (err.status === 409) this.setState({errorMessage: 'Title already in use'});
                else this.setState({errorMessage: 'An error occurred editing the post!'})
            })
    }

    private handleCancel() {
        this.props.location.state ? this.props.history.push('/main/post/' + this.props.location.state.post.id, {post: this.props.location.state.post}) :
            this.props.history.push('/main/home');
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
        if (this.state.postDataLoaded) {
            return (
                <div className={"main-container"}>
                    <div className={'editpost-card'}>

                        <div className={'editpost-header'}>
                            <img className={'trendz-logo'} src={logo} alt={''}/>
                            <div className={'divisor'}/>
                            <div className={'editpost-title'}>Edit Post</div>
                        </div>
                        <Formik
                            initialValues={{
                                title: this.state.title,
                                description: this.state.description,
                                link: this.state.link
                            }}
                            onSubmit={values => this.handleEditPost(values.title, values.description, values.link)}
                            validationSchema={editPostSchema}
                        >
                            {(props) => (
                                <div className={'form-container'}>
                                    <div className={'editpost-body'}>
                                        <div className={'editpost-field'}>
                                            <TrendzInput
                                                width='775px'
                                                label={'Title'}
                                                onChange={props.handleChange('title')}
                                                value={props.values.title}
                                                onFocus={() => this.handleOnFocus('title')}
                                                onBlur={() => !props.errors.title && this.handleOnBlur('title')}
                                            />
                                            <div
                                                className={'error-message'}>{this.state.titleTouched && props.errors.title}</div>
                                        </div>
                                        <div className={'editpost-field'}>
                                            <TrendzMultilineInput
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
                                        <div className={'editpost-field'}>
                                            <TrendzInput
                                                width='775px'
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
                                    <div className={'editpost-footer'}>
                                        <div className={'error-message'}>{this.state.errorMessage}</div>
                                        <div className={'footer-buttons'}>
                                            <TrendzButton
                                                title={'Submit'}
                                                onClick={() => props.values.title === '' && props.values.description === '' ?
                                                    this.setState({errorMessage: 'Please, complete fields before submitting'}) : props.handleSubmit()}
                                                disabled={!!(props.errors.title || props.errors.description)}
                                            />
                                            <TrendzButton
                                                title={'Cancel'}
                                                onClick={() => this.handleCancel()}
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
        else{
            return( <div>Loading...</div>)
        }
    }


}

export default withRouter(EditPost)