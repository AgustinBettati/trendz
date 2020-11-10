import React, {Component} from 'react'
import './Post.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";
import {deletePost, getPostData} from "../../api/PostApi";
import {MdThumbDown, MdThumbUp, MdDelete, MdModeEdit} from 'react-icons/md';
import {PostType, TopicType} from "../types/types";
import {Formik,} from 'formik';
import * as yup from 'yup';
import TimeAgo from 'react-timeago'

import {createComment, deleteComment, editComment} from "../../api/CommentApi";
import {getTopic} from "../../api/TopicApi";
import {toast} from "react-toastify";
import {deleteVote, downvotePost, upvotePost} from "../../api/VoteAPI";

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
    showDeleteCommentModal: boolean,
    commentToDelete: string,
    commentToDeleteId: number,
    commentToDeleteUserId: number,
    deleteCommentErrorMessage:string,
    showDeleteComment:boolean,
    hoverIndex:number,
    toEditComment: number,
    editContent: string,
    editErrorMessage: string,
    handleVoteError: string,
    clickedUpvote: boolean,
    clickedDownvote: boolean
    upvotes: any[],
    downvotes: any[],
    numberOfUpvotes:number,
    numberOfDownvotes:number,

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
                upvotes:[],
                downvotes:[],
            },
            topic: {
                id: -1,
                title: '',
                description: ''
            },
            topicErrorMessage: '',
            postErrorMessage: '',
            showModal: false,
            comments: [],
            errorMessage: '',
            successMessage: '',
            commentTouched: false,
            showDeleteCommentModal: false,
            commentToDelete:'',
            commentToDeleteId:-1,
            commentToDeleteUserId:-1,
            deleteCommentErrorMessage:'',
            showDeleteComment:false,
            hoverIndex:-1,
            toEditComment: -1,
            editContent: '',
            editErrorMessage: '',
            handleVoteError: '',
            clickedUpvote: false,
            clickedDownvote: false,
            upvotes: [],
            downvotes: [],
            numberOfUpvotes:0,
            numberOfDownvotes:0,


        }
    };

    componentDidMount() {
        const userId=parseJwt(localStorage.getItem('token')).userId

        getPostData(this.props.match.params.id)
            .then((res) => {

                this.setState({
                    post: res,
                    comments: res.comment,
                    upvotes: res.upvotes,
                    downvotes: res.downvotes,
                    numberOfUpvotes:res.upvotes.length,
                    numberOfDownvotes:res.downvotes.length,
                    clickedDownvote:res.downvotes.includes(Number(userId)),
                    clickedUpvote:res.upvotes.includes(Number(userId))
                })
                console.log(this.state.clickedUpvote)
                console.log(this.state.clickedDownvote)
                getTopic(res.topicId)
                    .then((res) => this.setState({topic: res}))
                    .catch((err) => this.setState({topicErrorMessage: err}))
            })
            .catch((err) => this.setState({postErrorMessage: err}))
    }


    handlePostComment = (comment: string) => {
        createComment(comment, this.props.match.params.id)
            .then((res) => {
                const newComments = [...this.state.comments]
                newComments.unshift(res)
                this.setState({errorMessage: '', successMessage: 'Comment succesfully created', comments: newComments});
            })
            .catch(() => {
                this.setState({successMessage: '', errorMessage: 'Ooops! something went wrong!'});
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

    setIsShown= (index:number ) => {
        this.setState({showDeleteComment:!this.state.showDeleteComment, hoverIndex:index})
    }

    handleCancelDeleteComment = () => {
        this.setState({showDeleteCommentModal: false})
    }

    handleConfirm = () => {
        deletePost(this.props.location.state ? this.props.location.state.post.id : this.props.match.params.id)
            .then(() => {
                this.handleCancel()
                this.handleTopicNavigation()
                toast('Post deleted successfully!')
            })
            .catch(() => {
                this.handleCancel()
                toast.error('An error occurred deleting the post!')
            })
    }

    handleConfirmDeleteComment = () => {
       deleteComment(this.state.commentToDeleteId)
            .then(() => {
                this.setState( { comments: [...this.state.comments].filter(comment => comment.id !== this.state.commentToDeleteId), showDeleteCommentModal:false } )

            })
           .catch(() => this.setState({deleteCommentErrorMessage: 'An error occurred deleting your comment!'}))

    }

     handleDownvote = () => {
        if(this.state.clickedDownvote){
            deleteVote(this.props.match.params.id)
                .then(() => {
                    this.setState({clickedDownvote:false, numberOfDownvotes:this.state.numberOfDownvotes-1})
                })
                .catch(() => this.setState({handleVoteError: 'An error occurred'}))
        }
         if (this.state.clickedUpvote){
             this.setState({numberOfUpvotes:this.state.numberOfUpvotes-1})
         }
        this.setState({clickedUpvote: false})
        if(!this.state.clickedDownvote) {
            downvotePost(this.props.match.params.id)
                .then(() => {
                    this.setState({clickedDownvote:true})
                })
                .catch(() => this.setState({handleVoteError: 'An error occurred'}))
            this.setState({numberOfDownvotes:this.state.numberOfDownvotes+1})
        }
    }
    handleUpvote = () => {
        if(this.state.clickedUpvote){
            deleteVote(this.props.match.params.id)
                .then(() => {
                    this.setState({clickedUpvote:false, numberOfUpvotes:this.state.numberOfUpvotes-1})
                })
                .catch(() => this.setState({handleVoteError: 'An error occurred'}))
        }
        if (this.state.clickedDownvote){
            this.setState({numberOfDownvotes:this.state.numberOfDownvotes-1})
        }
        this.setState({clickedDownvote: false})
        if(!this.state.clickedUpvote) {
            upvotePost(this.props.match.params.id)
                .then(() => {
                    this.setState({clickedUpvote: true})
                })
                .catch(() => this.setState({handleVoteError: 'An error occurred'}))
            this.setState({numberOfUpvotes: this.state.numberOfUpvotes+1})
        }
    }

    handleDelete = () => {
        this.setState({showModal: true})
    }

    handleTopicNavigation = () => {
        this.props.history.push('/main/topic/' + this.state.topic.id, {topic : this.state.topic})
    }

    handleEditClick = (index: number, content: string) => {
        this.setState({toEditComment: index, editContent: content, editErrorMessage: ''})
    }

    handleEditCommentChange = (value: string) => {
        this.setState({editContent: value})
    }

    handleCancelEdit = () => {
        this.setState({editContent: '', toEditComment: -1})
    }

    handleEditSave = () => {
        editComment(this.state.editContent, this.state.comments[this.state.toEditComment].id)
            .then(res => {
                const newComments = [...this.state.comments]
                newComments[this.state.toEditComment] = res
                this.setState({comments: newComments, editErrorMessage: ''})
                this.handleCancelEdit()
            })
            .catch(() => this.setState({editErrorMessage: 'An error occurred editing your comment!'}))
    }

    handlePostEdit = () => {
        this.props.history.push('/main/editPost', {post : this.state.post})
    }

    formatter = (value: number, unit: string, suffix: string) => {
        if (unit === 'second') return 'Just now';
        if (value > 1) return value + ' ' + unit + 's' + ' ' + suffix;
        return value + ' ' + unit + ' ' + suffix
    }

    handleProfileNavigation = (id: number) => {
        this.props.history.push('/main/viewProfile/' + id)
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
                <Modal
                    isOpen={this.state.showDeleteCommentModal}
                    onRequestClose={this.handleCancelDeleteComment.bind(this)}
                    shouldCloseOnOverlayClick={true}
                    className={'modal'}
                    overlayClassName={'overlay'}
                >
                    <div className={'modal-text'}>
                        <span>{'You are about to delete your comment.'}</span>
                        <span>This action is irreversible, </span>
                        <span>do you wish to continue?</span>
                        <div className={'error-message'}>{this.state.deleteCommentErrorMessage}</div>
                    </div>
                    <div className={'modal-buttons'}>
                        <TrendzButton title={'Confirm'} onClick={this.handleConfirmDeleteComment.bind(this)}/>
                        <TrendzButton title={'Cancel'} color={'#df6052'} onClick={this.handleCancelDeleteComment}/>
                    </div>
                </Modal>
                <div className={'post-header-wrapper'}>
                    <div className={'header-text'}>
                        <span className={'post-subtitle'} onClick={() => this.handleTopicNavigation()}>
                            {'This post belongs to the topic: ' + this.state.topic.title}
                        </span>
                        <div className={'post-username-title'}>
                            <span className={'post-header-title'}>{this.state.post.title}</span>
                            <span className={'by'}>by</span>
                            <span className={'post-header-username'} onClick={() => this.handleProfileNavigation(this.state.post.userId)}>{this.state.post.username}</span>
                        </div>
                        {
                            this.state.post.link &&
                            <a href={this.state.post.link.match(/^http?:/) ? this.state.post.link : '//' + this.state.post.link}
                               target={'_blank'}>{this.state.post.link}</a>
                        }
                    </div>
                    <div className={'post-buttons-container'}>
                        {
                            parseJwt(localStorage.getItem('token')).userId == this.state.post.userId &&
                            <TrendzButton
                                title={'Edit post'}
                                onClick={() => this.handlePostEdit()}
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
                            { this.state.clickedUpvote&&
                                <MdThumbUp size={20} color={'#00B090'} className={'like-icon'}
                                           onClick={() => this.handleUpvote()}/>
                            }
                            {!this.state.clickedUpvote&&
                                <MdThumbUp size={20} color={'grey'} className={'like-icon'}
                                           onClick={() => this.handleUpvote()}/>
                            }
                            <span className={'like-value'}>{this.state.numberOfUpvotes}</span>
                        </div>
                        <div className={'like-container'}>
                            { this.state.clickedDownvote&&

                            <MdThumbDown size={20} color={'#C13D3D'} className={'like-icon'}
                                         onClick={() => this.handleDownvote()}/>
                        }
                            {!this.state.clickedDownvote&&
                                <MdThumbDown size={20} color={'grey'} className={'like-icon'}
                                             onClick={() => this.handleDownvote()}/>
                            }

                            <span className={'like-value'}>{this.state.numberOfDownvotes}</span>
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
                                <textarea style={{width: "100%", resize: 'none'}} placeholder={'Your comment here..'}
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
                                <div key={index} className={'comment-card'}
                                     onMouseEnter={() => {this.setIsShown(index)
                                                          this.setState({commentToDeleteUserId:comment.userId})
                                     }}
                                     onMouseLeave={() => this.setIsShown(-1)}>
                                    <div className={'comment-header-container'}>
                                    <div className={'comment-header'}>
                                        <div onClick={() => this.handleProfileNavigation(comment.userId)} className={'clickUser'}>
                                            {comment.username + ' - '}
                                        </div>
                                        <TimeAgo date={comment.creationDate} formatter={this.formatter} />

                                        {comment.editDate && <span style={{color: '#818181', marginLeft: 5}}>edited</span>}

                                    </div>
                                        <div className={'comment-buttons-container'}>

                                        {

                                            (parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') ||
                                                parseJwt(localStorage.getItem('token')).userId == this.state.commentToDeleteUserId)  && this.state.hoverIndex==index &&

                                                <MdDelete
                                                    color={'#DF6052'}
                                                    size={20}
                                                    onClick={() => this.setState({showDeleteCommentModal: true, commentToDelete: comment.content, commentToDeleteId:comment.id})}
                                                />

                                        }
                                        {
                                            (parseJwt(localStorage.getItem('token')).userId == comment.userId) && this.state.hoverIndex==index &&
                                            <MdModeEdit
                                                color={'black'}
                                                size={20}
                                                onClick={() => this.handleEditClick(index, comment.content)}
                                            />

                                        }
                                        </div>

                                    </div>


                                    {
                                        this.state.toEditComment === index ?
                                            <div className={'edit-comment'}>
                                                <textarea
                                                    onChange={(e) => this.handleEditCommentChange(e.target.value)}
                                                    value={this.state.editContent}
                                                />
                                                <div>
                                                    <button className={'save-button'} onClick={() => this.handleEditSave()}>Save</button>
                                                    <button className={'cancel-button'} onClick={() => this.handleCancelEdit()}>Cancel</button>
                                                    <span style={{color: '#DF6052', marginLeft: 5}}>{this.state.editErrorMessage}</span>
                                                </div>
                                            </div> :
                                        <div className={'comment-body'}>{comment.content}</div>
                                    }
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
            props.resetForm();
        };
    }


}

export default withRouter(Post);
