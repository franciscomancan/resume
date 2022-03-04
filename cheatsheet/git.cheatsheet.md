Terms
	'head' = the branch you currently have checked out
	'origin' == ?? (remote repository from which the current was cloned)

General Info
	'git help ????' = will open an html man page for the wildcard concept, works for any concept
	'git help glossary' = opens glossary page for all terms, concepts, usage, etc... for git
	'git log' = list version history for branch
	'git log --follow filename.java' = list history for a single file, including renames
	'git reflog' = history of when the tips of branches and other references were updated in the local repository

Configuration
	'git config --list'
	'git config --global user.name "That Guy"' = sets username for commits and other related references
	'git config --global user.email "thatguy@someplace.com"' = sets email for commits/visibility
	'git config --global color.ui false' = supposed to turn off console coloring
	'git config --global core.safecrlf false' = supposed to turn off the repetitive warning on console for CRLF line endings
	'git config --global core.editor "'c:/program files/sublime text 3/subl.exe' -w"' = associates sublime as commitment editor (to avoid using the vi-like rainbow-colored command window)
	'git config --global commit.template ~/.gitmessage.txt' = applies the conent of the text file as the default commmit message
 	'git config --global alias.unstage 'reset --'' = set an alias for common actions

 	Remove most color from terminal by adding the following to ~/.gitconfig:

 	[color]    
	    ui = false
	    branch = false
	    diff = false
	    interactive = false
	    status = false
	    log = false

Project Management
	'git clone https://thatguy-somewhere@bitbucket.org/company/fixtheworkd.git destinationFolder' = clones a repository to a custom-named folder (will not work for non-empty folders)	

	**Following command sequence necessary to checkout code into a non-empty dir or where git already initiated:
		'git init'			(only if not yet initialized)
		'git remote add origin PATH/TO/REPO'
		'git fetch'
		'git checkout -t origin/master'

Branching
	'git branch' = show local branches, * next to active branch
	'git branch -a' = shows all branches, local and remote
	'git checkout -b someNewLocalBranch' = creates (and changes to) a new local branch by the given name
	'git push --set-upstream origin someNewLocalBranch' = sets (and creates if necessary) the remote branch to push to
	'git branch --set-upstream-to=origin/<branch> master' = sets a given (checked-out) branch upstream destination
	'git branch -D branchName' = deletes a particular branch
	'git push origin --delete feature/login' = deletes a remote branch

Managing Change Sets
	'git commit --amend' = alter the commit message of the last commit
	'git stash list' = list all stashes
	'git stash pop' = apply the stash on the top of the stack
	'git stash save "this is a comment for the stash"' = make a stash and apply a comment
	'git stash drop stash@{1}' = remove stash by name

Merging
	'git cherry-pick 91e91f1ad' = will commit the change indicated by the hash into the branch that is currently checked out

Conflicts
  'git mergetool' = assuming there's an existing conflict, this will open the configured tool to resolve the conflict

Reverting/Unstage
	'git clean -xfd' = does a force delete of all untracked files and directories, also ignores the .ignore settings (so ignored files will also be deleted)  
	'git revert 4945db2' = reverts changes from a specific commit
	'git revert HEAD~3' = Revert the changes specified by the fourth last commit in HEAD and create a new commit with the reverted changes.
	'git revert -n master~5..master~2' = Revert the changes done by commits from the fifth last commit in master (included) to the third last commit in 
		master (included), but do not create any commit with the reverted changes. The revert only modifies the working tree and the index.

	'git checkout someFile' = revert any locally made changes to a given file
	'git checkout -- PolicyCenter903/some/file/you/want/back/to/revert' = will revert at the file level
	'git reset HEAD <file>..." =  to unstage a given file
	'git push origin anthonysBranch --force' = forces push to remote, ignoring fast-forward errors (overwrites, deletes, etc.); may be helpful in undoing pushed commits

	** When all else not working, try:
		'git reset -- someFile'
  		'git checkout sameFile'

Searching/Viewing:
	'git grep someKindOfString' = searches file in git for string
	'git grep -n someFunctionOrWhatever' = search for cases of the string and include line numbers in output (similar to 'real' grep)
	'git grep --count someString' = rollup count of instances per file
	'git diff-tree --no-commit-id --name-only -r bd61ad98' = lists all files in a given commit
	'git diff branch_1..branch_2' = produce the diff between the tips of the two branches
	'git diff branch_1...branch_2' = find the diff from common ancestor of 2 branches, use three dots
	'git log --diff-filter=D --summary' = lists all deleted files throughout the change history, grouped by commit
	'git log --author=Anthony' = return all commits where Anthony is in the author attribute; Add --all if you intend to search all branches and not just the current commit's ancestors in your repo.	
	'git log -SZLIB_BUF_MAX --oneline' = If we want to find out for example when the ZLIB_BUF_MAX constant was originally introduced,
		we can tell Git to only show us the commits that either added or removed that string with the -S option.
	'git ls-tree -r HEAD' = will list all files ('blobs' only?) from root, recursively, along with a hash of the file itself; look for duplicates of hashes to detect..

Pull Requests:
	To diff a specific pull request locally (https://github.com/github/hub/issues/631):
		$ git fetch origin "+refs/heads/master:refs/remotes/origin/master" "+refs/pull/42/head:refs/pr/42"
		$ git difftool "$(git merge-base origin/master pr/42)" pr/42
		This, of course, wouldn't work if the base branch for #42 was something other than "master". But you get the gist.


*************************************************************************************************************************** BELOW IS JUST USAGE OUTPUT FROM GIT COMMAND LINE 
            C:\copperpoint.space\gitcopperpoint\gw7>git help
            usage: git [--version] [--help] [-C <path>] [-c name=value]
                       [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]
                       [-p | --paginate | --no-pager] [--no-replace-objects] [--bare]
                       [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]
                       <command> [<args>]

            These are common Git commands used in various situations:

            start a working area (see also: git help tutorial)
               clone      Clone a repository into a new directory
               init       Create an empty Git repository or reinitialize an existing one

            work on the current change (see also: git help everyday)
               add        Add file contents to the index
               mv         Move or rename a file, a directory, or a symlink
               reset      Reset current HEAD to the specified state
               rm         Remove files from the working tree and from the index

            examine the history and state (see also: git help revisions)
               bisect     Use binary search to find the commit that introduced a bug
               grep       Print lines matching a pattern
               log        Show commit logs
               show       Show various types of objects
               status     Show the working tree status

            grow, mark and tweak your common history
               branch     List, create, or delete branches
               checkout   Switch branches or restore working tree files
               commit     Record changes to the repository
               diff       Show changes between commits, commit and working tree, etc
               merge      Join two or more development histories together
               rebase     Reapply commits on top of another base tip
               tag        Create, list, delete or verify a tag object signed with GPG

            collaborate (see also: git help workflows)
               fetch      Download objects and refs from another repository
               pull       Fetch from and integrate with another repository or a local branch
               push       Update remote refs along with associated objects
            
            'git help -a' and 'git help -g' list available subcommands and some concept guides. See 'git help <command>' or 'git help <concept>'
              to read about a specific subcommand or concept.
