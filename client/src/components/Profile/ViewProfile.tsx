import React, {Component} from 'react';
import './ViewProfile.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import Avatar from "react-avatar";
import {getUserData} from "../../api/UserApi";
import {MdThumbDown, MdThumbUp} from "react-icons/md";
import {parseJwt} from "../Routing/utils";

export type Props = RouteComponentProps<any> & {}

export type State = {
    posts: any[],
    userName: string,
    email: string,
    deleted: boolean,
    getDataError: string
}

class ViewProfile extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            posts: [],
            userName: '',
            email: '',
            deleted: false,
            getDataError: ''
        }
    };

    componentDidMount() {
        getUserData(this.props.match.params.id)
            .then((res) => {
                    console.log(res)
                    this.setState({userName: res.userInfo.username, email: res.userInfo.email, posts: res.posts, deleted: res.userInfo.deleted})
                }
            )
            .catch((err) => this.setState({getDataError: 'An error occurred fetching profile data'}))
    }

    handlePostSelection = (post: any) => {
        this.props.history.push('/main/post/' + post.id)
    }

    voted = (votes: number[]) => {
        return votes.includes(Number(parseJwt(localStorage.getItem('token')).userId))
    }

    render() {
        return (
            <div className={'view-profile-container'}>
                <div className={'view-profile-header'}>
                    <div className={'name-avatar-container'}>
                        <div className={'avatar'}>
                            <Avatar name={this.state.userName}/>
                        </div>
                        <div className={'name-email'}>
                            <div>
                                <span className={'name'}>{this.state.userName}</span>
                                <span className={'deleted'}>{this.state.deleted && '(DELETED)'}</span>
                            </div>
                            <span className={'email'}>{this.state.email}</span>
                        </div>
                    </div>
                </div>
                <div className={'latest-posts-wrapper'}>
                    <div className={'latest-posts-title'}>Latest posts</div>
                    <div className={'latest-posts-container'}>
                        {
                            this.state.posts.length === 0 ?
                                'There are no posts to show' :
                            this.state.posts.map((post, index) => (
                                <div className={'post-card-wrapper'} key={index} style={{width: '100%'}}>
                                    <div className={'post-card'} style={{height: 85}} onClick={() => this.handlePostSelection(post)}>
                                        <div className={'post-card-header'}>
                                            <div className={'post-title'}>{post.title}</div>
                                            <div className={'post-topic'}>{post.topicTitle}</div>
                                        </div>
                                        <div className={'profile-post-card-body'} style={{color: 'black'}}>{post.description}</div>
                                        <div className={'likes-container'}>
                                            <div className={'like-container'}>
                                                <MdThumbUp size={20} color={this.voted(post.upvotes) ? '#00B090' : 'grey'} className={'like-icon'}/>
                                                <span className={'like-value'} style={{color: 'black'}}>{post.upvotes.length}</span>
                                            </div>
                                            <div className={'like-container'}>
                                                <MdThumbDown size={20} color={this.voted(post.downvotes) ? '#C13D3D' : 'grey'} className={'like-icon'}/>
                                                <span className={'like-value'} style={{color: 'black'}}>{post.downvotes.length}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                        }
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(ViewProfile)
