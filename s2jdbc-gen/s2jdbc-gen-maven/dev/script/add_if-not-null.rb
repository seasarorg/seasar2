$stdin.each do |line|
  if line =~ /command\.set[A-z]+\(([A-z]+)\);/
    puts "if (#{$1} != null)"
  end
  print line
end
