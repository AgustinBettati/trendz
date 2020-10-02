import React, {Component} from 'react'
import './Post.css';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {TrendzButton} from "../common/TrendzButton/TrendzButton";
import {parseJwt} from "../Routing/utils";
import Modal from "react-modal";

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
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'},
                {id: 0, author: 'Jhon Mark', body: 'This is the description for a humor post. asd asd asd asd sad asd as das das dasda sdasd asdasdasd asdasd asdas dasdasd asda sdas dasdasd asdas dasd asd as dasd asd asda.', date: '20/20/20 18:00 Hs'}
            ]
        }
    };

    handleCancel = () => {
        this.setState({showModal: false})
    }

    handleConfirm = () => {

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
                        <span className={'topic-title'}>{this.props.location.state.post.title}</span>
                        <span className={'topic-subtitle'}>{this.props.location.state.post.link}</span>
                    </div>
                    {
                        parseJwt(localStorage.getItem('token')).role.includes('ROLE_ADMIN') &&
                        <TrendzButton
                            title={'Delete post'}
                            onClick={() => this.handleDelete()}
                            color={'#DF6052'}
                        />
                    }
                </div>
            </div>
        )
    }
}

export default withRouter(Post);
