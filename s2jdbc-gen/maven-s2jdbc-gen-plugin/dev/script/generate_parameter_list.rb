require 'set'

files = {}

ARGV.each do |fname|
  open(fname, 'r') do |f|
    methods = Set.new
    f.each do |line|
      if line =~ /public\s+void+\s+set(\w+)\((\w+)/
        methods << "#{$1}.#{$2}"
      end
    end
    files[fname] = methods
  end
end

all_names = files.values.inject(Set.new) do |res, names|
  res + names
end

puts (files.collect { |pair| "\t" + pair[0] }).join('')

all_names.sort.each do |name|
  puts name + (files.collect do |pair|
    "\t" + (pair[1].member?(name) ? 'œ' : '')
  end.join(''))
end
