import React, {Component} from 'react'
import './Post.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";
import {deletePost} from "../../api/PostApi";
import {MdThumbDown, MdThumbUp} from 'react-icons/md';

export type Props = RouteComponentProps<any> & {}

export type State = {
    comments: any[],
    showModal: boolean
}

class Post extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            showModal: false,
            comments: [
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the body for a comment post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'}
            ]
        }
    };

    handleCancel = () => {
        this.setState({showModal: false})
    }

    handleConfirm = () => {
        deletePost(this.props.location.state.post.id).then(() =>
            this.props.history.push('/main/topic', {topic : this.props.location.state.topic}))
    }

    handleDelete = () => {
        this.setState({showModal: true})
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
                        <span>{'You are about to delete ' + this.props.location.state.post.title + '.'}</span>
                        <span>This action is irreversible, </span>
                        <span>do you wish to continue?</span>
                    </div>
                    <div className={'modal-buttons'}>
                        <TrendzButton title={'Confirm'} onClick={this.handleConfirm.bind(this)}/>
                        <TrendzButton title={'Cancel'} color={'#DF6052'} onClick={this.handleCancel.bind(this)}/>
                    </div>
                </Modal>
                <div className={'post-header-wrapper'}>
                    <div className={'header-text'}>
                        <span className={'topic-subtitle'}>
                            {'This post belongs to the topic: ' + this.props.location.state.topic.title}
                        </span>
                        <span className={'post-header-title'}>{this.props.location.state.post.title}</span>
                        {
                            this.props.location.state.post.link &&
                            <a href={this.props.location.state.post.link} target={'_blank'}>{this.props.location.state.post.link}</a>
                        }
                    </div>
                    <div className={'post-buttons-container'}>
                        {
                            parseJwt(localStorage.getItem('token')).userId == this.props.location.state.post.userId &&
                            <TrendzButton
                                title={'Edit post'}
                                onClick={() => null}
                                color={'#00B090'}
                            />
                        }
                        {
                            (parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') ||
                                parseJwt(localStorage.getItem('token')).userId == this.props.location.state.post.userId) &&
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
                        {this.props.location.state.post.description}
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
                    <div className={'post-comments-title'}>
                        {'Comments (' + this.state.comments.length + ')'}
                    </div>
                    <div className={'post-comments-container'}>
                        {
                            this.state.comments.map((comment, index) => (
                                <div key={index} className={'comment-card'}>
                                    <div className={'comment-header'}>
                                        {comment.author + ' - ' + comment.date}
                                    </div>
                                    <div className={'comment-body'}>{comment.body}</div>
                                </div>
                            ))
                        }
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(Post);
