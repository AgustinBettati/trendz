import React, {Component} from 'react';
import './ViewProfile.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import Avatar from "react-avatar";
import {getUserData} from "../../api/UserApi";

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
                                <div className={'post-card-wrapper'} key={index}>
                                    <div className={'post-card'} style={{height: 70}} onClick={() => this.handlePostSelection(post)}>
                                        <div className={'post-card-header'}>
                                            <div className={'post-title'}>{post.title}</div>
                                            <div className={'post-topic'}>{post.topicTitle}</div>
                                        </div>
                                        <div className={'post-card-body'} style={{color: 'black'}}>{post.description}</div>
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
