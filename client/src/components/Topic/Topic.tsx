import React, {Component} from 'react'
import './Topic.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import ReactPaginate from "react-paginate";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";
import {deleteTopic} from "../../api/TopicApi";
import { getTopicPosts} from "../../api/TopicApi";
import {TopicType} from "../types/types";

export type Props = RouteComponentProps<any> & {}

export type State = {
    posts: any[],
    showModal: boolean,
    currentPage: number,
    topic: TopicType
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
            posts: [
                {id: 0, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 1, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 2, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 3, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 4, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 5, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 6, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 7, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 8, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 9, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 10, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
                {id: 11, link: 'https://trello.com/c/pjHHwlbl/42-issue-0144-visualizacion-de-un-topic',title: 'This is the title of the post', author: 'Jhon Mark', description: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.'},
            ],
            currentPage: 0
        }
    };

    componentDidMount() {
        this.props.location.state && this.setState({topic: this.props.location.state.topic})
        getTopicPosts(this.props.match.params.id).then(res => this.setState({posts: res}))
        // with get topic by id endpoint
        // this.props.location.state ? this.setState({topic: this.props.location.state.topic}) :
        //     getTopicData(this.props.match.params.id)
        //         .then((res) => this.setState({topic: res}))
        //         .catch((err) => this.setState({topicErrorMessage: err}))
    }

    handleCancel = () => {
        this.setState({showModal: false})
    }

    handleConfirm = () => {
        deleteTopic(this.state.topic.id).then(() => this.props.history.push('/main/home'))
    }

    handleDelete = () => {
        this.setState({showModal: true})
    }

    renderPosts = (currentPage: number) => {
        return this.state.posts.slice(currentPage*3, currentPage*3+3)
    }

    handlePageClick = (data: {selected: number}) => {
        this.setState({currentPage: data.selected})
    }

    handlePostSelection = (post: any) => {
        this.props.history.push('/main/post/' + post.id, {post: post, topic : this.state.topic})
    }

    handlePostLinkClick = (link: string, e: any) => {
        e.stopPropagation();
        window.open(link, '_blank');
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
                        <span className={'topic-subtitle'}>{this.state.topic.description}</span>
                    </div>
                    {
                        parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') &&
                        <TrendzButton
                            title={'Delete topic'}
                            onClick={() => this.handleDelete()}
                            color={'#DF6052'}
                        />
                    }
                </div>
                <div className={'posts-container'}>
                    {
                        this.state.posts.length &&
                        this.renderPosts(this.state.currentPage).map((post, index) => (
                            <div className={'post-card-wrapper'} key={index}>
                                <div className={'post-card'} onClick={() => this.handlePostSelection(post)}>
                                    <div className={'post-card-header'}>
                                        <div className={'post-card-title'}>
                                            <div className={'post-title'}>
                                                {post.title}
                                            </div>
                                            <div className={'post-author'}>
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
                        pageCount={this.state.posts.length/3}
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
