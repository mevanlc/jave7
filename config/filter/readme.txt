This folder contains the configuration files for the internal filter algorithm. The algorithm
is being used by many tools in JavE, e.g. the generic line tool.

The most important configuration file is lineartcleaner.filter, since it contains filter
rules for cleaning up line art images. It could be extended to create even better results.

File format: Each file consists of any number of pairs of source and result masks:

 ,------ Source mask
 |   ,-- Result mask
 V   V

? ' ??/
 '  ?/?
!?? ???

If the filter algorithm detects a pattern matching the source mask, it will replace it by using
the pattern from the result mask. There are special characters:

In the source mask:
  !  indicates that there must be any character different from space at this position
  ?  indicates that it does not matter what character exists at this position

In the result mask:
  ?  indicates that the character at this position will not be modified

So the example above will change this image:
      ,
     ,
    /
into:
     /
    /
   /

Note that there is no way to escape the special characters, so the filter algorithm is not able
to make modifications with ? and ! characters in the source rules and ? in the result.