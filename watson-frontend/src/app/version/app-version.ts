export interface AppVersion {
  build: {
    version: string;
    time: string;
    host: string;
    user: {
      name: string;
      email: string;
    }
  };
  git: {
    tags: string;
    branch: string;
    dirty: string;
    remoteOriginUrl: string;
    commit: {
      commitId: string;
      commitIdAbbrev: string;
      describe: string;
      describeShort: string;
      time: string;
      user: {
        name: string;
        email: string;
      },
      message: {
        full: string;
        short: string;
      }
    },
    closestTag: {
      name: string;
      commitCount: string;
    }
  };
}
