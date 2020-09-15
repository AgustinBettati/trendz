import React, {Component} from 'react'
import './Home.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import glasses from '../../assets/glasses.png';
import ReactPaginate from "react-paginate";
import {parseJwt} from "../Routing/utils";

export type Props = RouteComponentProps<any> & {}

export type State = {
    topics: {title: string, description: string}[]
}

class Home extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            topics: [
                {title: 'Humor', description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'},
                {title: 'Humor', description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'},
                {title: 'Humor', description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'},
                {title: 'Humor', description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'},
                {title: 'Humor', description: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.'},
            ]
        }
    };

    render() {
        return (
            <div className={'home-container'}>
                <div className={'home-header'}>
                    <div className={'header-text'}>
                        <span className={'home-title'}>Topics</span>
                        <span className={'home-subtitle'}>Here you can find every topic and read more about those that interest you.</span>
                    </div>
                    {
                        parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') &&
                        <TrendzButton title={'Create topic'} onClick={() => this.props.history.push('/main/createTopic')}/>
                    }
                </div>
                <div className={'topics-container'}>
                    {
                        this.state.topics.map((topic) => (
                            <div className={'topic-card'}>
                                <div className={'topic-header'}>
                                    {topic.title}
                                </div>
                                <div className={'topic-divider'}/>
                                <div className={'topic-body'}>
                                    {topic.description}
                                </div>
                                <div className={'topic-footer'}>
                                    {/* onClick to navigate to topic */}
                                    <img src={glasses} alt={''} onClick={() => null}/>
                                </div>
                            </div>
                        ))
                    }
                </div>
                <div className={'home-footer'}>
                    <ReactPaginate
                        pageCount={10}
                        pageRangeDisplayed={3}
                        marginPagesDisplayed={3}
                        previousLabel={"<< Previous"}
                        nextLabel={"Next >>"}
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

export default withRouter(Home);
