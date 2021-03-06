import React, { Component } from 'react'
import 'react-toastify/dist/ReactToastify.css';
import './App.css'
import {RouteComponentProps, withRouter} from "react-router-dom";
import { Switch, Route } from 'react-router-dom'
import Register from "../Register/Register";
import PrivateRoute from "../Routing/PrivateRoute";
import {AppFrame} from "../Routing/AppFrame";
import Profile from "../Profile/Profile";
import Login from "../Login/Login";
import EditProfile from "../EditProfile/EditProfile";
import Home from "../Home/Home";
import CreateTopic from "../CreateTopic/CreateTopic";
import CreatePost from "../CreatePost/CreatePost";
import EditPost from "../EditPost/EditPost";
import Topic from "../Topic/Topic";
import Post from "../Post/Post";
import Search from "../Search/Search";
import ViewProfile from "../Profile/ViewProfile";
import {ToastContainer} from "react-toastify";

type MatchProps = {
  match: {
    url: string
  }
}

export type Props = RouteComponentProps<any> & {}

class App extends Component<Props>{
  render() {
    return (
      <div className="app">
        <ToastContainer position={'top-center'} autoClose={2500}/>
        <Switch>
          <Route exact={true} path="/" component={Login}/>
          <Route exact={true} path="/register" component={Register}/>
          <PrivateRoute path={'/main'} component={({match}: MatchProps) => ([
              <AppFrame>
                <Switch>
                  <Route exact path={`${match.url}/profile`} component={Profile}/>
                  <Route exact path={`${match.url}/home`} component={Home}/>
                  <Route exact path={`${match.url}/topic/:id`} component={Topic}/>
                  <Route exact path={`${match.url}/post/:id`} component={Post}/>
                  <Route exact path={`${match.url}/editProfile`} component={EditProfile}/>
                  <Route exact path={`${match.url}/createTopic`} component={CreateTopic}/>
                  <Route exact path={`${match.url}/createPost`} component={CreatePost}/>
                  <Route exact path={`${match.url}/editPost`} component={EditPost}/>
                  <Route exact path={`${match.url}/search/:toSearch`} component={Search}/>
                  <Route exact path={`${match.url}/viewProfile/:id`} component={ViewProfile}/>
                </Switch>
              </AppFrame>
          ])}/>
        </Switch>
      </div>
    )
  }
}

export default withRouter(App)
