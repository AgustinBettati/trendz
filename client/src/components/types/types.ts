export type UserType = {
  id: number;
  name: string;
  surname: string;
  email: string;
}

export type TopicType = {
  id: number,
  title: string,
  description: string
}

export type PostType = {
  title: string,
  description: string,
  link: string,
  topicId: number,
  userId: number,
  username: string,
  upvotes: number,
  downvotes: number,
}