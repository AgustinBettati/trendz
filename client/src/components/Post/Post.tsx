import React, {Component} from 'react'
import './Post.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";
import {deletePost, getPostData} from "../../api/PostApi";
import {MdThumbDown, MdThumbUp} from 'react-icons/md';
import {PostType, TopicType} from "../types/types";
import {Formik,} from 'formik';
import * as yup from 'yup';
import {createComment} from "../../api/CommentApi";


export type Props = RouteComponentProps<any> & {}

export type State = {
    comments: any[],
    showModal: boolean,
    post: PostType,
    topic: TopicType,
    topicErrorMessage: string,
    postErrorMessage: string,
    errorMessage: string,
    successMessage: string,
    commentTouched: boolean,


}
const postCommentSchema = yup.object({
    comment: yup.string().max(10000, "Comment can be up to 10000 characters long"),

})


class Post extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            post: {
                title: '',
                description: '',
                link: '',
                topicId: -1,
                userId: -1,
                username: '',
            },
            topic: {
                id: -1,
                title: '',
                description: ''
            },
            topicErrorMessage: '',
            postErrorMessage: '',
            showModal: false,
            comments: [
                {id: 0, editDate: '20/20/20 18:00 Hs', username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: '20/20/20 18:00 Hs', username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: '20/20/20 18:00 Hs', username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: null, username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: null, username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: null, username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: null, username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'},
                {id: 0, editDate: null, username: 'Jhon Mark', content: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', creationDate: '20/20/20 18:00 Hs'}
            ],
            errorMessage: '',
            successMessage: '',
            commentTouched: false
        }
    };

    componentDidMount() {
        this.props.location.state ?
            this.setState({post: this.props.location.state.post, topic: this.props.location.state.topic}) :
            this.getViewData()
    }

    getViewData = () => {
        getPostData(this.props.match.params.id)
            .then((res) => {
                this.setState({post: res})
                // with get topic by id endpoint
                /*getTopicData(res.topicId)
                    .then((res) => this.setState({topic: res}))
                    .catch((err) => this.setState({topicErrorMessage: err}))*/
            })
            .catch((err) => this.setState({postErrorMessage: err}))
    }

    handlePostComment = (comment: string) => {
        createComment(comment, this.props.location.state.topic.id)
            .then((res) => {
                this.setState({errorMessage: '', successMessage: 'Comment succesfully created'});
                //TODO refresh comments


            })
            .catch((e) => {
                this.setState({successMessage: '', errorMessage: 'Ooops! something went wrong!'});
                console.log(e)
            })

    }

    handleOnFocus = (prop: string) => {
        this.setState({errorMessage: '', successMessage: ''})
        switch (prop) {
            case 'comment':
                this.setState({commentTouched: true});
                break;

        }
    }

    handleOnBlur = (prop: string) => {
        switch (prop) {
            case 'comment':
                this.setState({commentTouched: false});
                break;

        }
    }


    handleCancel = () => {
        this.setState({showModal: false})
    }

    handleConfirm = () => {
        deletePost(this.props.location.state ? this.props.location.state.post.id : this.props.match.params.id)
            .then(() => this.handleTopicNavigation())
    }

    handleDelete = () => {
        this.setState({showModal: true})
    }

    handleTopicNavigation = () => {
        this.props.history.push('/main/topic/' + this.state.topic.id, {topic : this.state.topic})
    }

    render() {
        return (
            <div className={'post-container'}>
                <Modal
                    isOpen={this.state.showModal}
                    onRequestClose={this.handleCancel.bind(this)}
                    shouldCloseOnOverlayClick={true}
                    className={'modal'}
                    overlayClassName={'overlay'}
                >
                    <div className={'modal-text'}>
                        <span>{'You are about to delete ' + this.state.post.title + '.'}</span>
                        <span>This action is irreversible, </span>
                        <span>do you wish to continue?</span>
                    </div>
                    <div className={'modal-buttons'}>
                        <TrendzButton title={'Confirm'} onClick={this.handleConfirm.bind(this)}/>
                        <TrendzButton title={'Cancel'} color={'#df6052'} onClick={this.handleCancel.bind(this)}/>
                    </div>
                </Modal>
                <div className={'post-header-wrapper'}>
                    <div className={'header-text'}>
                        <span className={'post-subtitle'} onClick={() => this.handleTopicNavigation()}>
                            {'This post belongs to the topic: ' + this.state.topic.title}
                        </span>
                        <span className={'post-header-title'}>{this.state.post.title}</span>
                        {
                            this.state.post.link &&
                            <a href={this.state.post.link} target={'_blank'}>{this.state.post.link}</a>
                        }
                    </div>
                    <div className={'post-buttons-container'}>
                        {
                            parseJwt(localStorage.getItem('token')).userId == this.state.post.userId &&
                            <TrendzButton
                                title={'Edit post'}
                                onClick={() => null}
                                color={'#00B090'}
                            />
                        }
                        {
                            (parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') ||
                                parseJwt(localStorage.getItem('token')).userId == this.state.post.userId) &&
                            <TrendzButton
                                title={'Delete post'}
                                onClick={() => this.handleDelete()}
                                color={'#DF6052'}
                            />
                        }
                    </div>
                </div>
                <div className={'post-body-wrapper'}>
                    <div className={'body-container'}>
                        {this.state.post.description}
                    </div>
                    <div className={'body-footer'}>
                        <div className={'like-container'}>
                            <MdThumbUp size={20} color={'#00B090'} className={'like-icon'}/>
                            <span className={'like-value'}>295</span>
                        </div>
                        <div className={'like-container'}>
                            <MdThumbDown size={20} color={'#C13D3D'} className={'like-icon'}/>
                            <span className={'like-value'}>24</span>
                        </div>
                    </div>
                </div>
                <div className={'post-comments-wrapper'}>
                    <div className={'post-comments-title'}>New Comment
                    </div>
                    <Formik
                        initialValues={{comment: ''}}
                        onSubmit={(values) => {}}
                        validationSchema={postCommentSchema}

                    >{(props) => (
                        <div>

                            <div className={'comment-body'}>
                            <textarea  placeholder={'Your comment here..'}
                                      onChange={props.handleChange('comment')}
                                      value={props.values.comment}
                                      onFocus={() => this.handleOnFocus('comment')}
                                      onBlur={() => !props.errors.comment && this.handleOnBlur('comment')}>

                            </textarea>
                                <div className={'error-message'}>{this.state.errorMessage}</div>
                                <div className={'success-message'}> {this.state.successMessage}</div>
                            </div>
                            <div className={'post-buttons-container'}>
                                <TrendzButton
                                    title={'Add comment'}
                                    onClick={this.getOnClick(props)}
                                    disabled={(props.values.comment == '')}
                                />
                                <TrendzButton
                                    title={'Cancel'}
                                    onClick={() => props.resetForm()}
                                />
                            </div>
                        </div>
                    )}
                    </Formik>
                    <div className={'post-comments-title'}>
                        {'Comments (' + this.state.comments.length + ')'}
                    </div>
                    <div className={'post-comments-container'}>
                        {
                            this.state.comments.map((comment, index) => (
                                <div key={index} className={'comment-card'}>
                                    <div className={'comment-header'}>
                                        {comment.username + ' - ' + comment.creationDate + ' '}
                                        {comment.editDate && <span style={{color: '#818181'}}>edited</span>}
                                    </div>
                                    <div className={'comment-body'}>{comment.content}</div>
                                </div>
                            ))
                        }
                    </div>
                </div>
            </div>
        )
    }

    private getOnClick(props: any & { submitForm: () => Promise<any> }) {
        return () => {
            this.handlePostComment(props.values.comment);
            let commentArray = {
                id: 0,
                username: parseJwt(localStorage.getItem('token')).mail,
                content: props.values.comment,
                creationDate: this.getCurrentDate('/')
            };
            let comments = this.state.comments;
            comments.push(commentArray);
            props.resetForm();

        };
    }

     private getCurrentDate(separator=''){

        let newDate = new Date()
        let date = newDate.getDate();
        let month = newDate.getMonth() + 1;
        let year = newDate.getFullYear();

        return `${year}${separator}${month<10?`0${month}`:`${month}`}${separator}${date}`
    }
}


export default withRouter(Post);
