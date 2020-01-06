// const _apiUrl = '192.168.1.203:3000'; // local IP
const _apiUrl = '192.168.1.8:3000'; // local IP

const _httpApiUrl = `http://${_apiUrl}`;

const _defaultHeaders = {
  'Accept': 'application/json',
  'Content-Type': 'application/json',
};

let _token;

export const setToken = value => _token = value;

export const httpGet = path =>
  _withErrorHandling(
    fetch(`${_httpApiUrl}/${path}`, {
      method: 'GET',
      headers: _buildHeaders(),
    }),
  );

export const httpPost = (path, payload) =>
  _withErrorHandling(
    fetch(`${_httpApiUrl}/${path}`, {
      method: 'POST',
      body: JSON.stringify(payload),
      headers: _buildHeaders(),
    }),
  );

export const httpPut = (path, payload) =>
  _withErrorHandling(
    fetch(`${_httpApiUrl}/${path}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
      headers: _buildHeaders(),
    }),
  );

export const httpDelete = (path) =>
  _withErrorHandling(
    fetch(`${_httpApiUrl}/${path}`, {
      method: 'DELETE',
      headers: _buildHeaders(),
    }),
  );

const _buildHeaders = () => {
  const headers = {..._defaultHeaders};
  if (_token) {
    headers.Authorization = `Bearer ${_token}`;
  }
  return headers;
};

const _defaultIssue = {issue: [{error: 'Unexpected error'}]};

const _withErrorHandling = fetchPromise =>
  fetchPromise
    .then(response => {
      console.log('p-1');

      console.log(JSON.stringify(response, null, 2));
      const x = Promise.all([response.ok, response.json()]);
      console.log('p0');
      return x
    })
    .then(([responseOk, responseJson]) => {
      console.log('p1');
      if (responseOk) {
        console.log('p2');
        return responseJson;
      }
      console.log('p3');
      const message = (responseJson || _defaultIssue).issue
        .map(it => it.error)
        .join('\n');
      console.log('p4');
      throw new Error(message);
    });
