export const getLogger = tag => (message, ...args) => console.log(`[${tag}] ${message}. args =`, args.length > 0 ? args : '');
