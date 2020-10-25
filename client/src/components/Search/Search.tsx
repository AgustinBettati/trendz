import React, {Component} from 'react'
import './Search.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import Checkbox from "../common/Checkbox/Checkbox";
import {search} from "../../api/Search";

export type Props = RouteComponentProps<any> & {}

export type State = {
    filters: string[],
    posts: any[],
    topics: any[],
    searchError: string
}

class Search extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            filters: ['topics', 'posts'],
            posts: [],
            topics: [],
            searchError: ''
        }
    };

    componentDidMount() {
        this.searchContent(this.props.match.params.toSearch)
        this.props.history.listen((location) =>
            this.searchContent(location.pathname.split('/')[location.pathname.split('/').length-1])
        )
    }

    searchContent = (toSearch: string) => {
        search(toSearch)
            .then((res) => this.setState({posts: res.posts, topics: res.topics}))
            .catch((err) => this.setState({searchError: err}))
    }

    handleCheckboxClick = (filter: string) => {
        if (this.state.filters.includes(filter)) {
            this.setState({filters: [...this.state.filters].filter(f => f !== filter)})
        } else {
            const newFilters = [...this.state.filters]
            newFilters.unshift(filter)
            this.setState({filters: newFilters})
        }
    }

    handlePostSelection = (post: any) => {
        this.props.history.push('/main/post/' + post.id)
    }

    handleTopicSelection = (topic: any) => {
        this.props.history.push('/main/topic/' + topic.id)
    }

    handlePostLinkClick = (link: string, e: any) => {
        e.stopPropagation();
        window.open(link, '_blank');
    }

    render() {
        return (
            <div className={'search-container'}>
                <div className={'filters-container'}>
                    <div className={'filter'}>
                        <div className={'filter-label'}>Topics</div>
                        <Checkbox handleChange={() => this.handleCheckboxClick('topics')} selected={this.state.filters.includes('topics')}/>
                    </div>
                    <div className={'filter'}>
                        <div className={'filter-label'}>Posts</div>
                        <Checkbox handleChange={() => this.handleCheckboxClick('posts')} selected={this.state.filters.includes('posts')}/>
                    </div>
                </div>
                <div className={'result-container'}>
                    {
                        this.state.filters.includes('topics') &&
                            <div className={'result-wrapper'}>
                                <div className={'result-title'}>Topics</div>
                                {
                                    this.state.topics.length === 0 ?
                                        <div className={'result-message'}>No topics match your search!</div> :
                                        <div className={'search-result-container'}>
                                            {
                                                this.state.topics.map((topic, index) => (
                                                    <div className={'topic-card-wrapper'} key={index}>
                                                        <div className={'search-topic-card'} onClick={() => this.handleTopicSelection(topic)}>
                                                            <div className={'search-topic-card-header'}>
                                                                <div className={'search-topic-title'}>{topic.title}</div>
                                                                <div className={'find-posts'}>Find posts</div>
                                                            </div>
                                                            <div className={'search-topic-description'}>{topic.description}</div>
                                                        </div>
                                                    </div>
                                                ))
                                            }
                                        </div>
                                }
                            </div>
                    }
                    {
                        this.state.filters.includes('posts') &&
                        <div className={'result-wrapper'}>
                            <div className={'result-title'}>Posts</div>
                            {
                                this.state.posts.length === 0 ?
                                    <div className={'result-message'}>No posts match your search!</div> :
                                    <div className={'search-result-container'}>
                                        {
                                            this.state.posts.map((post, index) => (
                                                <div className={'post-card-wrapper'} key={index}>
                                                    <div className={'post-card'} onClick={() => this.handlePostSelection(post)}>
                                                        <div className={'post-card-header'}>
                                                            <div className={'post-card-title'}>
                                                                <div className={'post-title'}>{post.title}</div>
                                                                <div className={'post-author'}>{'by ' + post.username}</div>
                                                            </div>
                                                            <div className={'post-topic'}>TopicTitle {/*Need topic title in the response*/}</div>
                                                        </div>
                                                        <div className={'post-card-body'}>{post.description}</div>
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
                            }
                        </div>
                    }
                </div>
            </div>
        )
    }
}

export default withRouter(Search);
