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
    .then(response => Promise.all([response.ok, response.json()]))
    .then(([responseOk, responseJson]) => {
      if (responseOk) {
        return responseJson;
      }
      const message = (responseJson || _defaultIssue).issue
        .map(it => it.error)
        .join('\n');
      throw new Error(message);
    });
