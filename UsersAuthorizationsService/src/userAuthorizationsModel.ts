const mongoose = require('mongoose');

const userAuthorizationsSchema = new mongoose.Schema({
  targetID : String,
  authorizations : [String]
});

export const userAuthorizations = mongoose.model('usersAuthorizations', userAuthorizationsSchema);
