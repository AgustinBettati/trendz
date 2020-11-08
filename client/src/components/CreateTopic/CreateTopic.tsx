import React, {Component} from 'react';
import "./CreateTopic.css"
import {TrendzInput} from "../common/TrendzInput/TrendzInput";
import {TrendzMultilineInput} from "../common/TrendzMultilineInput/TrendzMultilineInput";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import logo from '../../assets/TrendzLogo.png';
import {Formik} from 'formik';
import * as yup from 'yup';
import {withRouter} from "react-router-dom";
import {RouteComponentProps} from 'react-router-dom';
import {createTopic} from "../../api/TopicApi";
import {toast} from "react-toastify";

export type Props = RouteComponentProps<any> & {}

export type State = {
    errorMessage: string,
    successMessage: string,
    titleTouched: boolean,
    descriptionTouched: boolean,
}

const createTopicSchema = yup.object({
    title: yup.string().required('Title cannot be empty').matches(/^[0-9a-zA-Z ]*$/,"Title must be alfanumeric").max(40,"Title can be up to 40 characters long").min(2,"Title must be at least 2 characters long"),
    description: yup.string().max(40000, "Description can be up to 40000 characters long"),
})

class CreateTopic extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            errorMessage: '',
            successMessage: '',
            titleTouched: false,
            descriptionTouched: false,
        }
    }

    handleSubmitTopic = (title: string, description: string) => {
        createTopic(title,description)
            .then(() => {
                this.setState({errorMessage: '', successMessage: 'Topic successfully created'});
                this.props.history.push('/main/home')
                toast('Topic successfully created!')
            })
            .catch((err) => {
                if (err.status === 409) toast.error('Title already in use!')
                else toast.error('An error occurred creating the topic!')
            })
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
        }
    }

    render() {
        return (
            <div className={"main-container"}>
                <div className={'createtopic-card'}>
                    <div className={'createtopic-header'}>
                        <img className={'trendz-logo'} src={logo} alt={''}/>
                        <div className={'divisor'}/>
                        <div className={'createtopic-title'}>Create Topic</div>
                    </div>
                    <Formik
                        initialValues={{title: '', description: ''}}
                        onSubmit={values => this.handleSubmitTopic(values.title, values.description)}
                        validationSchema={createTopicSchema}
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
                                </div>
                                <div className={'createpost-footer'}>
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

export default withRouter(CreateTopic)