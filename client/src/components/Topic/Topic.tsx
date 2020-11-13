import React, {Component} from 'react'
import './Topic.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import ReactPaginate from "react-paginate";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";
import {deleteTopic, getTopic} from "../../api/TopicApi";
import { getTopicPosts} from "../../api/TopicApi";
import {TopicType} from "../types/types";
import {toast} from "react-toastify";

export type Props = RouteComponentProps<any> & {}

export type State = {
    posts: any[],
    showModal: boolean,
    currentPage: number,
    topic: TopicType,
    topicErrorMessage: string,
    totalPages: number
}

class Topic extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            topic: {
                id: -1,
                title: '',
                description: ''
            },
            showModal: false,
            topicErrorMessage: '',
            posts: [],
            currentPage: 0,
            totalPages: 0
        }
    };

    componentDidMount() {
        this.props.location.state ? this.setState({topic: this.props.location.state.topic}) :
             getTopic(this.props.match.params.id)
                 .then((res) => this.setState({topic: res}))
                 .catch((err) => this.setState({topicErrorMessage: err}))
        this.getPosts()
    }

    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any) {
        if(prevState.currentPage !== this.state.currentPage) this.getPosts()
    }

    getPosts = () => {
        getTopicPosts(this.props.match.params.id, this.state.currentPage, 3).then(res => this.setState({posts: res.posts, totalPages: res.totalPages}))
    }

    handleCancel = () => {
        this.setState({showModal: false})
    }

    handleConfirm = () => {
        deleteTopic(this.state.topic.id)
            .then(() => {
                this.handleCancel()
                this.props.history.push('/main/home')
                toast('Topic deleted successfully!')
            })
            .catch(() => {
                this.handleCancel()
                toast.error('An error occurred deleting the topic!')
            })
    }

    handleDelete = () => {
        this.setState({showModal: true})
    }

    handlePageClick = (data: {selected: number}) => {
        this.setState({currentPage: data.selected})
    }

    handlePostSelection = (post: any) => {
        this.props.history.push('/main/post/' + post.id, {post: post, topic : this.state.topic})
    }

    handlePostCreation = () => {
        this.props.history.push('/main/createPost', {topic : this.state.topic})
    }

    handlePostLinkClick = (link: string, e: any) => {
        e.stopPropagation();
        window.open(link, '_blank');
    }

    handleProfileNavigation = (e: any, id: number) => {
        e.stopPropagation()
        this.props.history.push('/main/viewProfile/' + id)
    }

    render() {
        return (
            <div className={'topic-container'}>
                <Modal
                    isOpen={this.state.showModal}
                    onRequestClose={this.handleCancel.bind(this)}
                    shouldCloseOnOverlayClick={true}
                    className={'modal'}
                    overlayClassName={'overlay'}
                >
                    <div className={'modal-text'}>
                        <span>{'You are about to delete ' + this.state.topic.title + ' topic.'}</span>
                        <span>This action is irreversible, </span>
                        <span>do you wish to continue?</span>
                    </div>
                    <div className={'modal-buttons'}>
                        <TrendzButton title={'Confirm'} onClick={this.handleConfirm.bind(this)}/>
                        <TrendzButton title={'Cancel'} color={'#DF6052'} onClick={this.handleCancel.bind(this)}/>
                    </div>
                </Modal>
                <div className={'topic-header-wrapper'}>
                    <div className={'header-text'}>
                        <span className={'topic-title'}>{this.state.topic.title}</span>
                    </div>


                    <div className={'topic-buttons-container'}>
                        {
                        parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') &&
                        <TrendzButton
                            title={'Delete topic'}
                            onClick={() => this.handleDelete()}
                            color={'#DF6052'}
                        />
                        }
                        <TrendzButton title={'Create Post'} onClick={() => this.handlePostCreation()}/>
                    </div>
                </div>
                <div className={'post-body-wrapper'}>
                    <div className={'topic-body-container'}>
                        {this.state.topic.description}
                    </div>
                </div>

                <div className={'posts-container'}>

                    {
                        this.state.posts.length &&
                        this.state.posts.map((post, index) => (
                            <div className={'post-card-wrapper'} key={index}>
                                <div className={'post-card'} onClick={() => this.handlePostSelection(post)}>
                                    <div className={'post-card-header'}>
                                        <div className={'post-card-title'}>
                                            <div className={'post-title'}>
                                                {post.title}
                                            </div>
                                            <div className={'post-author'} onClick={(e) => this.handleProfileNavigation(e, post.userId)}>
                                                {'by ' + post.username}
                                            </div>
                                        </div>
                                        <div className={'post-topic'}>
                                            {this.state.topic.title}
                                        </div>
                                    </div>
                                    <div className={'post-card-body'}>
                                        {post.description}
                                    </div>
                                    <div className={'post-card-footer'}>
                                        <div className={'post-link'} onClick={(e) => this.handlePostLinkClick(post.link, e)}>
                                            {post.link}
                                        </div>
                                        <div className={'read-more'}>Read more</div>
                                    </div>
                                </div>
                            </div>
                        ))
                    }
                </div>
                <div className={'topic-footer'}>
                    <ReactPaginate
                        onPageChange={this.handlePageClick}
                        pageCount={this.state.totalPages}
                        pageRangeDisplayed={5}
                        marginPagesDisplayed={2}
                        previousLabel={"<"}
                        nextLabel={">"}
                        breakLabel={'...'}
                        containerClassName={"pagination"}
                        previousLinkClassName={"previous_page"}
                        nextLinkClassName={"next_page"}
                        disabledClassName={"disabled"}
                        activeClassName={"active"}
                    />
                </div>
            </div>
        )
    }
}

export default withRouter(Topic);
