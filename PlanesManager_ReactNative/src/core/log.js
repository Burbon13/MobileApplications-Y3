export const getLogger = tag => (message, ...args) => {
  if (args.length > 0) {
    console.log(`[${tag}] ${message};`, args);
  } else {
    console.log(`[${tag}] ${message}`);
  }
};
